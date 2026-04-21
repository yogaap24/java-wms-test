package com.warehouse.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

// data item yang dikembalikan ke client
@Data
@Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private List<VariantResponse> variants;
}
