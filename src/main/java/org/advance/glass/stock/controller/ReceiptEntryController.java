package org.advance.glass.stock.controller;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.advance.glass.stock.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipt-entry")
public class ReceiptEntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<Object> createReceiptEntry(@RequestBody EntryReqDto entryReqDto) {
        entryService.createReceiptEntry(entryReqDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Receipt entry created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
