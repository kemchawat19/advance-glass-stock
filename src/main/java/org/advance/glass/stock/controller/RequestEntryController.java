package org.advance.glass.stock.controller;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.advance.glass.stock.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request-entry")
public class RequestEntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<Object> createRequestEntry(@RequestBody EntryReqDto entryReqDto) {
        entryService.createRequestEntry(entryReqDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Request entry created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Object> confirmRequestEntry(@PathVariable Long id) {
        entryService.confirmRequestEntry(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Confirm request entry successfully.");

        return ResponseEntity.ok(response);
    }
}
