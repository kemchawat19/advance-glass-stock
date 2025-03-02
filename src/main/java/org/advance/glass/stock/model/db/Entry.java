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
@Table(name = "entry", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"type", "entryNumber"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 20)
    @Column(nullable = false)
    private String entryNumber;

    @NotNull
    @Size(max = 20)
    @Column(nullable = false)
    private String type;

    //can change from input
    @NotNull
    @Column(nullable = false)
    private LocalDateTime entryDate;

    @Size(max = 20)
    private String jobNumber;

    @Size(max = 20)
    private String referenceNumber;

    @Size(max = 10)
    private String processStatus;

    private LocalDateTime confirmedDate;

    private int supplierId;

    @Size(max = 255)
    private String supplierName;

    //add from input
    @Size(max = 20)
    private String supplierInvoice;

    private int employeeId;

    @Size(max = 100)
    private String employeeName;

    @Size(max = 100)
    private String description;

    @OneToMany(mappedBy = "entry", cascade = CascadeType.ALL)
    private List<EntryDetail> entryDetailList;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;
}
