package com.warehouse.exception;

// exception saat data tidak ditemukan di database
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
