package org.advance.glass.stock.controller;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.Entry;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.advance.glass.stock.service.EntryService;
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

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<Entry> createReceiptEntry(@RequestBody EntryReqDto dto) {
        Entry created = entryService.createReceiptEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
