package com.warehouse.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

// data varian yang dikembalikan ke client
@Data
@Builder
public class VariantResponse {
    private Long id;
    private Long itemId;
    private String name;
    // harga sudah di-resolve: pakai harga varian jika ada, jika tidak pakai harga item
    private BigDecimal price;
    private Integer stock;
}
