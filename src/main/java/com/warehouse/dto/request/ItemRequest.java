package com.warehouse.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

// data yang diterima saat buat atau update item
@Data
public class ItemRequest {

    @NotBlank(message = "Nama item tidak boleh kosong")
    private String name;

    private String description;

    @NotNull(message = "Harga tidak boleh kosong")
    @DecimalMin(value = "0.01", message = "Harga harus lebih dari 0")
    private BigDecimal price;

    // stok awal, default 0
    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stock = 0;
}
