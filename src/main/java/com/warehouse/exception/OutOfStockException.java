package com.warehouse.exception;

// exception saat stok tidak cukup untuk memenuhi penjualan
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}
