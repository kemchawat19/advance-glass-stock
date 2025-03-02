package org.advance.glass.stock.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntryReqDto {
    // Common header fields
    private String entryNumber;      // Unique identifier for the entry
    private String type;             // E.g., "RECEIPT", "REQUEST", "RETURN"
    private LocalDateTime entryDate; // When the entry occurred
    private String jobNumber;        // Optional job number
    private String processStatus;           // E.g., "PENDING", "COMPLETED"
    private String referenceNumber;  // External or internal reference

    // Fields that may be used for certain entry types
    private Integer supplierId;      // E.g., for receipts
    private String supplierName;     // E.g., for receipts
    private String supplierInvoice;  // E.g., for receipts
    private Integer employeeId;      // Employee processing the entry
    private String employeeName;
    private String description;

    // List of line items for the entry
    private List<EntryDetailDto> entryDetailDtoList;
}
