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
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "PRODUCT_SEQ", allocationSize = 1)
    private Long id;

    @NotNull
    @Size(max = 6)
    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @NotNull
    @Size(max = 255)
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Size(max = 100)
    @Column(name = "product_group")
    private String productGroup;

    @Size(max = 50)
    @Column(name = "product_unit")
    private String productUnit;

    @Size(max = 10)
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createTimeStamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;
}
