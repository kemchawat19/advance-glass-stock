package org.advance.glass.stock.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateReceiptEntryReqDto {
    private String receiptNo;
    private String supplier;
    private LocalDateTime importDate;
    private String status;
    private List<Detail> details;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long stockId;
        private int quantity;
        private BigDecimal unitCost;
        private BigDecimal totalCost;
    }
}
