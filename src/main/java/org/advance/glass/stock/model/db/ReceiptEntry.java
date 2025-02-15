package org.advance.glass.stock.model.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "receipt_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique receipt number for this import transaction.
    @NotNull
    @Size(max = 20)
    @Column(name = "receipt_no", nullable = false, unique = true)
    private String receiptNo;

    // Supplier information.
    @NotNull
    @Size(max = 100)
    @Column(name = "supplier", nullable = false)
    private String supplier;

    // The date and time when the goods are imported.
    @NotNull
    @Column(name = "import_date", nullable = false)
    private LocalDateTime importDate;

    // Status can be used to track the process (e.g., COMPLETED).
    @NotNull
    @Size(max = 20)
    private String status;

    // One-to-many relation with the detail records.
    @OneToMany(mappedBy = "receiptEntry", cascade = CascadeType.ALL)
    private List<ReceiptEntryDetail> receiptEntryDetailList;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;
}
