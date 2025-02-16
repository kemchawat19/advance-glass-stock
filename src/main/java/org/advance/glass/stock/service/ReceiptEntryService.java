package org.advance.glass.stock.service;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.ReceiptEntry;
import org.advance.glass.stock.model.db.ReceiptEntryDetail;
import org.advance.glass.stock.model.db.Stock;
import org.advance.glass.stock.model.db.StockTransaction;
import org.advance.glass.stock.model.request.CreateReceiptEntryReqDto;
import org.advance.glass.stock.repository.ReceiptEntryRepository;
import org.advance.glass.stock.repository.StockRepository;
import org.advance.glass.stock.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceiptEntryService {

    private final ReceiptEntryRepository receiptEntryRepository;
    private final StockRepository stockRepository;
    private final StockTransactionRepository stockTransactionRepository;

    @Transactional
    public ReceiptEntry createReceiptEntry(CreateReceiptEntryReqDto dto) {
        // Map the header fields.
        ReceiptEntry receiptEntry = ReceiptEntry.builder()
                .receiptNo(dto.getReceiptNo())
                .supplier(dto.getSupplier())
                .importDate(dto.getImportDate())
                .status(dto.getStatus())
                .build();

        // Map each detail in the DTO to a ReceiptEntryDetail entity.
        if (dto.getDetails() != null && !dto.getDetails().isEmpty()) {
            List<ReceiptEntryDetail> details = dto.getDetails().stream().map(detailDto ->
                    ReceiptEntryDetail.builder()
                            // Create a Stock reference with only the id.
                            .stock(Stock.builder().id(detailDto.getStockId()).build())
                            .quantity(detailDto.getQuantity())
                            .unitCost(detailDto.getUnitCost())
                            .totalCost(detailDto.getTotalCost())
                            .build()
            ).collect(Collectors.toList());
            receiptEntry.setReceiptEntryDetailList(details);
        }

        // Save the header. With cascade, all associated details will be persisted.
        ReceiptEntry savedEntry = receiptEntryRepository.save(receiptEntry);

        // For each detail, update stock and log a stock transaction.
        if (savedEntry.getReceiptEntryDetailList() != null) {
            savedEntry.getReceiptEntryDetailList().forEach(detail -> {
                // Retrieve the Stock record.
                Stock stock = stockRepository.findById(detail.getStock().getId())
                        .orElseThrow(() -> new RuntimeException("Stock not found"));
                // Increase the stock quantity.
                stock.setQuantity(stock.getQuantity() + detail.getQuantity());
                stockRepository.save(stock);

                // Log a stock stock transaction.
                StockTransaction stockTransaction = StockTransaction.builder()
                        .stock(stock)
                        .transactionType("RECEIPT")
                        .quantityChange(detail.getQuantity())
                        .transactionDate(LocalDateTime.now())
                        .reference(savedEntry.getReceiptNo())
                        .description("Receipt Entry Detail")
                        .build();
                stockTransactionRepository.save(stockTransaction);
            });
        }
        return savedEntry;
    }
}
