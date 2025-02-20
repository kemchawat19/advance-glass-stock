package org.advance.glass.stock.service;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.constant.Type;
import org.advance.glass.stock.model.db.Entry;
import org.advance.glass.stock.model.db.EntryDetail;
import org.advance.glass.stock.model.db.Stock;
import org.advance.glass.stock.model.request.EntryDetailDto;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.advance.glass.stock.repository.EntryRepository;
import org.advance.glass.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Entry createReceiptEntry(EntryReqDto entryReqDto) {
        // Set type explicitly.
        entryReqDto.setType(Type.RECEIPT.name());
        Entry entry = mapEntry(entryReqDto);
        Entry savedEntry = entryRepository.save(entry);
        // For receipts, add quantities (multiplier +1)
        updateStock(savedEntry, +1);
        return savedEntry;
    }

    @Transactional
    public Entry createRequestEntry(EntryReqDto entryReqDto) {
        // Set type explicitly.
        entryReqDto.setType(Type.REQUEST.name());
        Entry entry = mapEntry(entryReqDto);
        Entry savedEntry = entryRepository.save(entry);
        // For requests, subtract quantities (multiplier -1)
        updateStock(savedEntry, -1);
        return savedEntry;
    }

    @Transactional
    public Entry confirmRequestEntry(Long requestEntryId) {
        // Retrieve and verify the entry.
        Entry requestEntry = entryRepository.findById(requestEntryId)
                .orElseThrow(() -> new RuntimeException("Request entry not found for ID: " + requestEntryId));

        if (!Type.REQUEST.name().equalsIgnoreCase(requestEntry.getType())) {
            throw new RuntimeException("Entry with ID " + requestEntryId + " is not a request entry.");
        }

        // Update status and confirmation date.
        requestEntry.setStatus("COMPLETED");
        requestEntry.setConfirmedDate(LocalDateTime.now());
        Entry updatedEntry = entryRepository.save(requestEntry);
        // Update stock (subtract requested quantities)
        updateStock(updatedEntry, -1);
        return updatedEntry;
    }

    @Transactional
    public Entry createReturnEntry(EntryReqDto entryReqDto) {
        // Set type explicitly.
        entryReqDto.setType(Type.RETURN.name());
        Entry entry = mapEntry(entryReqDto);
        Entry savedEntry = entryRepository.save(entry);
        // For receipts, add quantities (multiplier +1)
        updateStock(savedEntry, +1);
        return savedEntry;
    }

    // --- Helper Methods ---

    // Maps the unified DTO to an Entry entity.
    private Entry mapEntry(EntryReqDto entryReqDto) {
        return Entry.builder()
                .entryNumber(entryReqDto.getEntryNumber())
                .type(entryReqDto.getType())
                .entryDate(entryReqDto.getEntryDate())
                .jobNumber(entryReqDto.getJobNumber())
                .status(entryReqDto.getStatus())
                .referenceNumber(entryReqDto.getReferenceNumber())
                .supplierId(entryReqDto.getSupplierId() != null ? entryReqDto.getSupplierId() : 0)
                .supplierName(entryReqDto.getSupplierName())
                .supplierInvoice(entryReqDto.getSupplierInvoice())
                .employeeId(entryReqDto.getEmployeeId() != null ? entryReqDto.getEmployeeId() : 0)
                .employeeName(entryReqDto.getEmployeeName())
                .entryDetailList(mapEntryDetailList(entryReqDto.getEntryDetailDtoList()))
                .build();
    }

    // Converts a list of EntryDetailDto into a list of EntryDetail entities.
    private List<EntryDetail> mapEntryDetailList(List<EntryDetailDto> entryDetailDtoList) {
        if (entryDetailDtoList == null) return null;
        return entryDetailDtoList.stream()
                .map(entryDetailDto -> EntryDetail.builder()
                        .stock(Stock.builder().id(entryDetailDto.getStockId()).build())
                        .quantity(entryDetailDto.getQuantity())
                        .unit(entryDetailDto.getUnit())
                        .unitCost(entryDetailDto.getUnitCost())
                        .totalCost(entryDetailDto.getTotalCost())
                        .description(entryDetailDto.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    // Updates the stock for each detail line in the entry based on the multiplier.
    // Use multiplier +1 for receipts (increases stock) and -1 for requests (decreases stock).
    private void updateStock(Entry entry, int multiplier) {
        if (entry.getEntryDetailList() != null) {
            entry.getEntryDetailList().forEach(detail -> {
                Stock stock = stockRepository.findById(detail.getStock().getId())
                        .orElseThrow(() -> new RuntimeException("Stock not found for ID: " + detail.getStock().getId()));
                int newQuantity = stock.getQuantity() + multiplier * detail.getQuantity();
                stock.setQuantity(newQuantity);
                stockRepository.save(stock);
            });
        }
    }
}
