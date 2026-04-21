package com.warehouse.controller;

import com.warehouse.dto.request.ItemRequest;
import com.warehouse.dto.request.SellRequest;
import com.warehouse.dto.request.StockRequest;
import com.warehouse.dto.response.ApiResponse;
import com.warehouse.dto.response.ItemResponse;
import com.warehouse.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// endpoint REST untuk Item
@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // GET /api/items — ambil semua item
    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Berhasil mengambil semua item", itemService.findAll()));
    }

    // GET /api/items/{id} — ambil satu item
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Berhasil mengambil item", itemService.findById(id)));
    }

    // POST /api/items — buat item baru
    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> create(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Item berhasil dibuat", itemService.create(request)));
    }

    // PUT /api/items/{id} — update item
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Item berhasil diupdate", itemService.update(id, request)));
    }

    // DELETE /api/items/{id} — hapus item
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        itemService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Item berhasil dihapus", null));
    }

    // PUT /api/items/{id}/stock — update stok item (restock manual)
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<ItemResponse>> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Stok berhasil diupdate", itemService.updateStock(id, request)));
    }

    // POST /api/items/{id}/sell — jual item (kurangi stok)
    @PostMapping("/{id}/sell")
    public ResponseEntity<ApiResponse<ItemResponse>> sell(
            @PathVariable Long id,
            @Valid @RequestBody SellRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Penjualan berhasil", itemService.sell(id, request)));
    }
}
