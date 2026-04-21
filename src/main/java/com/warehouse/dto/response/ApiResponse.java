package com.warehouse.dto.response;

import lombok.Data;

// format standar semua response API
@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    // constructor manual agar type inference tidak bermasalah di static method
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // helper untuk response sukses
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    // helper untuk response error
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
