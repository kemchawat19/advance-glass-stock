package org.advance.glass.stock.service;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.constant.EntryType;
import org.advance.glass.stock.constant.ProcessStatus;
import org.advance.glass.stock.model.db.Entry;
import org.advance.glass.stock.model.db.EntryDetail;
import org.advance.glass.stock.model.db.Stock;
import org.advance.glass.stock.model.request.EntryDetailDto;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.advance.glass.stock.repository.EntryRepository;
import org.advance.glass.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final StockRepository stockRepository;
    private final ProductService productService;

    @Transactional
    public void createReceiptEntry(EntryReqDto entryReqDto) {
        System.out.println("createReceiptEntry = " + entryReqDto);
        // Set type explicitly.
        entryReqDto.setType(EntryType.RECEIPT.name());
        Entry entry = mapEntry(entryReqDto);
        String seq = generateNextEntryNumber(EntryType.RECEIPT.name());
        System.out.println("seq = "+seq);
        entry.setEntryNumber(seq);
        System.out.println("entry = "+entry);
        Entry savedEntry = entryRepository.save(entry);
        // For receipts, add quantities (multiplier +1)
        System.out.println("savedEntry = " + savedEntry);
        updateStock(savedEntry, +1);
    }

    @Transactional
    public void createRequestEntry(EntryReqDto entryReqDto) {
        // Set type explicitly.
        entryReqDto.setType(EntryType.REQUEST.name());
        entryReqDto.setProcessStatus(ProcessStatus.PENDING.name());
        Entry entry = mapEntry(entryReqDto);
        entry.setEntryNumber(generateNextEntryNumber(EntryType.REQUEST.name()));
        Entry savedEntry = entryRepository.save(entry);
        // For requests, subtract quantities (multiplier -1)
        updateStock(savedEntry, -1);
    }

    @Transactional
    public void confirmRequestEntry(Long requestEntryId) {
        // Retrieve and verify the entry.
        Entry requestEntry = entryRepository.findById(requestEntryId)
                .orElseThrow(() -> new RuntimeException("Request entry not found for ID: " + requestEntryId));

        if (!EntryType.REQUEST.name().equalsIgnoreCase(requestEntry.getEntryType())) {
            throw new RuntimeException("Entry with ID " + requestEntryId + " is not a request entry.");
        }

        // Update status and confirmation date.
        requestEntry.setProcessStatus(ProcessStatus.COMPLETED.name());
        requestEntry.setConfirmedDate(LocalDateTime.now());
        Entry updatedEntry = entryRepository.save(requestEntry);
        // Update stock (subtract requested quantities)
        updateStock(updatedEntry, -1);
    }

    @Transactional
    public void createReturnEntry(EntryReqDto entryReqDto) {
        // Set type explicitly.
        entryReqDto.setType(EntryType.RETURN.name());
        Entry entry = mapEntry(entryReqDto);
        entry.setEntryNumber(generateNextEntryNumber(EntryType.RETURN.name()));
        Entry savedEntry = entryRepository.save(entry);
        // For receipts, add quantities (multiplier +1)
        updateStock(savedEntry, +1);
    }

    // --- Helper Methods ---

    private Entry mapEntry(EntryReqDto entryReqDto) {
        Entry entry = Entry.builder()
                .entryType(entryReqDto.getType())
                .entryDate(entryReqDto.getEntryDate())
                .jobNumber(entryReqDto.getJobNumber())
                .processStatus(entryReqDto.getProcessStatus())
                .referenceNumber(entryReqDto.getReferenceNumber())
                .supplierId(entryReqDto.getSupplierId() != null ? entryReqDto.getSupplierId() : 0)
                .supplierName(entryReqDto.getSupplierName())
                .supplierInvoice(entryReqDto.getSupplierInvoice())
                .employeeId(entryReqDto.getEmployeeId() != null ? entryReqDto.getEmployeeId() : 0)
                .employeeName(entryReqDto.getEmployeeName())
                .description(entryReqDto.getDescription())
                .build();

        entry.setEntryDetailList(mapEntryDetailList(entryReqDto.getEntryDetailDtoList(), entry));
        return entry;
    }

    private List<EntryDetail> mapEntryDetailList(List<EntryDetailDto> entryDetailDtoList, Entry entry) {
        if (entryDetailDtoList == null) return null;

        return entryDetailDtoList.stream()
                .map(entryDetailDto -> EntryDetail.builder()
                        .entry(entry)
                        .stock(getOrCreateStock(entryDetailDto.getProductId()))
                        .quantity(entryDetailDto.getQuantity())
                        .unit(entryDetailDto.getUnit())
                        .unitPrice(entryDetailDto.getUnitPrice())
                        .amount(entryDetailDto.getUnitPrice().multiply(BigDecimal.valueOf(entryDetailDto.getQuantity())))
                        .build())
                .collect(Collectors.toList());
    }

    private Stock getOrCreateStock(Long productId) {
        return stockRepository.findByProductId(productId)
                .orElseGet(() -> stockRepository.save(
                        Stock.builder()
                                .product(productService.getProductById(productId))
                                .quantity(0)
                                .build()
                ));
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

    private String generateNextEntryNumber(String type) {
        String sequenceName = switch (EntryType.valueOf(type)) {
            case RECEIPT -> "receipt_seq";
            case REQUEST -> "request_seq";
            case RETURN -> "return_seq";
        };

        return String.format("%010d", entryRepository.getNextSequenceValue(sequenceName));
    }
}
