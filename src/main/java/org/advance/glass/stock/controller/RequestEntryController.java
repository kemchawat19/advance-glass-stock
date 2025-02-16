package org.advance.glass.stock.controller;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.RequestEntry;
import org.advance.glass.stock.model.request.CreateRequestEntryReqDto;
import org.advance.glass.stock.service.RequestEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/request-entry")
public class RequestEntryController {

    private final RequestEntryService requestEntryService;

    @PostMapping
    public ResponseEntity<RequestEntry> createRequest(@RequestBody CreateRequestEntryReqDto dto) {
        RequestEntry created = requestEntryService.createRequestEntry(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<RequestEntry> confirmRequest(@PathVariable Long id) {
        RequestEntry confirmed = requestEntryService.confirmRequestEntry(id);
        return ResponseEntity.ok(confirmed);
    }
}
