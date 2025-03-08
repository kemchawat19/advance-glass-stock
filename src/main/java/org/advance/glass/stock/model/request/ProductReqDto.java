package org.advance.glass.stock.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReqDto {
    private String productName;
    private String productGroup;
    private String productUnit;
    private String productStatus;
}
