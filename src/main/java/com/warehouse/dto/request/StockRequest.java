package com.warehouse.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// data yang diterima saat update stok secara manual
@Data
public class StockRequest {

    @NotNull(message = "Jumlah stok tidak boleh kosong")
    @Min(value = 0, message = "Stok tidak boleh negatif")
    private Integer stock;
}
