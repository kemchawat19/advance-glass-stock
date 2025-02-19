package org.advance.glass.stock.controller;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.Entry;
import org.advance.glass.stock.model.request.EntryReqDto;
import org.advance.glass.stock.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request-entry")
public class RequestEntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<Entry> createRequestEntry(@RequestBody EntryReqDto dto) {
        Entry created = entryService.createRequestEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Entry> confirmRequestEntry(@PathVariable Long id) {
        Entry confirmed = entryService.confirmRequestEntry(id);
        return ResponseEntity.ok(confirmed);
    }
}
