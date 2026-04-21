package com.warehouse.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// data yang diterima saat transaksi penjualan
@Data
public class SellRequest {

    @NotNull(message = "Jumlah tidak boleh kosong")
    @Min(value = 1, message = "Jumlah minimal 1")
    private Integer quantity;
}
