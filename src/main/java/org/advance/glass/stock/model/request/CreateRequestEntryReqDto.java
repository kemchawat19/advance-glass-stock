package org.advance.glass.stock.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRequestEntryReqDto {
    private String requestNo;
    private String requesterName;
    private LocalDateTime requestDate;
    private String status; // Typically "PENDING" when creating the request.
    private List<Detail> details;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long stockId;
        private int quantity;
        private String description;
    }
}
