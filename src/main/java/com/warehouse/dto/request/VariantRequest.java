package com.warehouse.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

// data yang diterima saat buat atau update varian
@Data
public class VariantRequest {

    @NotBlank(message = "Nama varian tidak boleh kosong")
    private String name;

    // harga varian, opsional (null = pakai harga item induk)
    @DecimalMin(value = "0.01", message = "Harga harus lebih dari 0")
    private BigDecimal price;

    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stock = 0;
}
