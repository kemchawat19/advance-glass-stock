package org.advance.glass.stock.service;

import lombok.RequiredArgsConstructor;
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
    public Entry createReceiptEntry(EntryReqDto dto) {
        // Set type explicitly.
        dto.setType("RECEIPT");
        Entry entry = mapEntry(dto);
        Entry savedEntry = entryRepository.save(entry);
        // For receipts, add quantities (multiplier +1)
        updateStock(savedEntry, +1);
        return savedEntry;
    }

    @Transactional
    public Entry createRequestEntry(EntryReqDto dto) {
        // Set type explicitly.
        dto.setType("REQUEST");
        Entry entry = mapEntry(dto);
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

        if (!"REQUEST".equalsIgnoreCase(requestEntry.getType())) {
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
    public Entry createReturnEntry(EntryReqDto dto) {
        // Set type explicitly.
        dto.setType("RETURN");
        Entry entry = mapEntry(dto);
        Entry savedEntry = entryRepository.save(entry);
        // For receipts, add quantities (multiplier +1)
        updateStock(savedEntry, +1);
        return savedEntry;
    }

    // --- Helper Methods ---

    // Maps the unified DTO to an Entry entity.
    private Entry mapEntry(EntryReqDto dto) {
        return Entry.builder()
                .entryNumber(dto.getEntryNumber())
                .type(dto.getType())
                .entryDate(dto.getEntryDate())
                .jobNumber(dto.getJobNumber())
                .status(dto.getStatus())
                .referenceNumber(dto.getReferenceNumber())
                .supplierId(dto.getSupplierId() != null ? dto.getSupplierId() : 0)
                .supplierName(dto.getSupplierName())
                .supplierInvoice(dto.getSupplierInvoice())
                .employeeId(dto.getEmployeeId() != null ? dto.getEmployeeId() : 0)
                .employeeName(dto.getEmployeeName())
                .entryDetailList(mapEntryDetailList(dto.getEntryDetailDtoList()))
                .build();
    }

    // Converts a list of EntryDetailDto into a list of EntryDetail entities.
    private List<EntryDetail> mapEntryDetailList(List<EntryDetailDto> entryDetailDtoList) {
        if (entryDetailDtoList == null) return null;
        return entryDetailDtoList.stream()
                .map(dto -> EntryDetail.builder()
                        .stock(Stock.builder().id(dto.getStockId()).build())
                        .quantity(dto.getQuantity())
                        .unit(dto.getUnit())
                        .unitCost(dto.getUnitCost())
                        .totalCost(dto.getTotalCost())
                        .description(dto.getDescription())
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
