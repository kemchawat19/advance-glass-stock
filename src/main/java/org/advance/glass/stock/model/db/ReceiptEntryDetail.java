package org.advance.glass.stock.model.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipt_entry_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptEntryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the header.
    @ManyToOne
    @JoinColumn(name = "receipt_entry_id", nullable = false)
    private ReceiptEntry receiptEntry;

    // Reference to the product in the stock.
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // The quantity of this product being imported.
    @NotNull
    private int quantity;

    // The cost per unit at the time of import.
    @Column(precision = 10, scale = 2)
    private BigDecimal unitCost;

    // Total cost = unitCost * quantity.
    @Column(precision = 10, scale = 2)
    private BigDecimal totalCost;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;
}
