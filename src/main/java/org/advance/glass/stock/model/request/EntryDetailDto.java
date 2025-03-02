package org.advance.glass.stock.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntryDetailDto {
    private String productCode;         // Reference to a product in stock
    private int quantity;         // Quantity of product in this line
    private String unit;          // Unit of measure, e.g., "pcs", "kg"
    private BigDecimal unitCost;  // Cost per unit
    private BigDecimal totalCost; // Calculated total cost (unitCost * quantity)
    private String description;   // Optional detail description
}
