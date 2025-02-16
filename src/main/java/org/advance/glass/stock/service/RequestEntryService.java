package org.advance.glass.stock.service;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.RequestEntry;
import org.advance.glass.stock.model.db.RequestEntryDetail;
import org.advance.glass.stock.model.db.Stock;
import org.advance.glass.stock.model.db.StockTransaction;
import org.advance.glass.stock.model.request.CreateRequestEntryReqDto;
import org.advance.glass.stock.repository.RequestEntryRepository;
import org.advance.glass.stock.repository.StockRepository;
import org.advance.glass.stock.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestEntryService {

    private final RequestEntryRepository requestEntryRepository;
    private final StockRepository stockRepository;
    private final StockTransactionRepository stockTransactionRepository;

    @Transactional
    public RequestEntry createRequestEntry(CreateRequestEntryReqDto dto) {
        // Map header fields.
        RequestEntry requestEntry = RequestEntry.builder()
                .requestNo(dto.getRequestNo())
                .requesterName(dto.getRequesterName())
                .requestDate(dto.getRequestDate())
                .status(dto.getStatus())
                .build();

        // Map each detail in the DTO to a RequestEntryDetail entity.
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            List<RequestEntryDetail> details = dto.getDetails().stream().map(detailDto ->
                    RequestEntryDetail.builder()
                            // Create a Stock reference with only the id.
                            .stock(Stock.builder().id(detailDto.getStockId()).build())
                            .quantity(detailDto.getQuantity())
                            .description(detailDto.getDescription())
                            .build()
            ).collect(Collectors.toList());
            requestEntry.setDetails(details);
        }

        // Save the header. With CascadeType.ALL (configured in the entity), details will be saved as well.
        return requestEntryRepository.save(requestEntry);
    }

    @Transactional
    public RequestEntry confirmRequestEntry(Long requestEntryId) {
        // Fetch the request entry.
        RequestEntry requestEntry = requestEntryRepository.findById(requestEntryId)
                .orElseThrow(() -> new RuntimeException("Request entry not found"));
        // Update its status and confirmation date.
        requestEntry.setStatus("COMPLETED");
        requestEntry.setConfirmedDate(LocalDateTime.now());
        RequestEntry updatedEntry = requestEntryRepository.save(requestEntry);

        // For each detail, update the stock (subtract the requested quantity) and log a dispatch transaction.
        if (updatedEntry.getDetails() != null) {
            updatedEntry.getDetails().forEach(detail -> {
                Stock stock = stockRepository.findById(detail.getStock().getId())
                        .orElseThrow(() -> new RuntimeException("Stock not found"));
                // Deduct the requested quantity.
                stock.setQuantity(stock.getQuantity() - detail.getQuantity());
                stockRepository.save(stock);

                // Log the stock transaction for dispatch.
                StockTransaction transaction = StockTransaction.builder()
                        .stock(stock)
                        .transactionType("DISPATCH")
                        .quantityChange(-detail.getQuantity())
                        .transactionDate(LocalDateTime.now())
                        .reference(updatedEntry.getRequestNo())
                        .description("Request Entry Confirmation")
                        .build();
                stockTransactionRepository.save(transaction);
            });
        }
        return updatedEntry;
    }
}
