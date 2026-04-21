package com.warehouse.controller;

import com.warehouse.dto.request.SellRequest;
import com.warehouse.dto.request.StockRequest;
import com.warehouse.dto.request.VariantRequest;
import com.warehouse.dto.response.ApiResponse;
import com.warehouse.dto.response.VariantResponse;
import com.warehouse.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// endpoint REST untuk Variant, nested di bawah item
@RestController
@RequestMapping("/api/items/{itemId}/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    // GET /api/items/{itemId}/variants — ambil semua varian milik item
    @GetMapping
    public ResponseEntity<ApiResponse<List<VariantResponse>>> getAll(@PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResponse.ok("Berhasil mengambil semua varian", variantService.findByItemId(itemId)));
    }

    // POST /api/items/{itemId}/variants — tambah varian ke item
    @PostMapping
    public ResponseEntity<ApiResponse<VariantResponse>> create(
            @PathVariable Long itemId,
            @Valid @RequestBody VariantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Varian berhasil dibuat", variantService.create(itemId, request)));
    }

    // PUT /api/items/{itemId}/variants/{variantId} — update varian
    @PutMapping("/{variantId}")
    public ResponseEntity<ApiResponse<VariantResponse>> update(
            @PathVariable Long itemId,
            @PathVariable Long variantId,
            @Valid @RequestBody VariantRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Varian berhasil diupdate", variantService.update(itemId, variantId, request)));
    }

    // DELETE /api/items/{itemId}/variants/{variantId} — hapus varian
    @DeleteMapping("/{variantId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long itemId,
            @PathVariable Long variantId) {
        variantService.delete(itemId, variantId);
        return ResponseEntity.ok(ApiResponse.ok("Varian berhasil dihapus", null));
    }

    // PUT /api/items/{itemId}/variants/{variantId}/stock — update stok varian
    @PutMapping("/{variantId}/stock")
    public ResponseEntity<ApiResponse<VariantResponse>> updateStock(
            @PathVariable Long itemId,
            @PathVariable Long variantId,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Stok varian berhasil diupdate", variantService.updateStock(itemId, variantId, request)));
    }

    // POST /api/items/{itemId}/variants/{variantId}/sell — jual varian
    @PostMapping("/{variantId}/sell")
    public ResponseEntity<ApiResponse<VariantResponse>> sell(
            @PathVariable Long itemId,
            @PathVariable Long variantId,
            @Valid @RequestBody SellRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Penjualan varian berhasil", variantService.sell(itemId, variantId, request)));
    }
}
