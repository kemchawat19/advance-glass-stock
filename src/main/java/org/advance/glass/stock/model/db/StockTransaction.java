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

@Entity
@Table(name = "stock_transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many transactions can refer to one product
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @NotNull
    @Size(max = 20)
    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // e.g., "RECEIPT", "DISPATCH"

    @NotNull
    @Column(name = "quantity_change", nullable = false)
    private int quantityChange;

    @CreationTimestamp
    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionDate;

    @Size(max = 50)
    private String reference;

    @Size(max = 255)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;
}
