package org.advance.glass.stock.controller;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.ReceiptEntry;
import org.advance.glass.stock.model.request.CreateReceiptEntryReqDto;
import org.advance.glass.stock.service.ReceiptEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipt-entry")
public class ReceiptEntryController {

    private final ReceiptEntryService receiptEntryService;

    @PostMapping
    public ResponseEntity<ReceiptEntry> createReceipt(@RequestBody CreateReceiptEntryReqDto dto) {
        ReceiptEntry created = receiptEntryService.createReceiptEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
