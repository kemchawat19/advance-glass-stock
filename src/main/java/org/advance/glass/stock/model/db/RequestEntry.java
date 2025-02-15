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
@Table(name = "request_entry")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique request number.
    @NotNull
    @Size(max = 20)
    @Column(name = "request_no", nullable = false, unique = true)
    private String requestNo;

    // Identifier for the person or department requesting the goods.
    @NotNull
    @Size(max = 100)
    @Column(name = "requester_name", nullable = false)
    private String requesterName;

    // Date when the request was made.
    @NotNull
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

    // Status: 'PENDING' initially, then 'COMPLETED' when goods are removed.
    @NotNull
    @Size(max = 20)
    private String status;

    // Date when the goods are confirmed removed.
    @Column(name = "confirmed_date")
    private LocalDateTime confirmedDate;

    // One-to-many relation with request details.
    @OneToMany(mappedBy = "requestEntry", cascade = CascadeType.ALL)
    private List<RequestEntryDetail> details;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;
}
