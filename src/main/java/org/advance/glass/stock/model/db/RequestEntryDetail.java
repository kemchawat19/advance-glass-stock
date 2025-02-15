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
@Table(name = "request_entry_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestEntryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference to the request header.
    @ManyToOne
    @JoinColumn(name = "request_entry_id", nullable = false)
    private RequestEntry requestEntry;

    // Reference to the product in stock.
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // The quantity requested for withdrawal.
    @NotNull
    private int quantity;

    // Optional note (for example, to record confirmation details).
    @Size(max = 255)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;
}
