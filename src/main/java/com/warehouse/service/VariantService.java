package com.warehouse.service;

import com.warehouse.dto.request.SellRequest;
import com.warehouse.dto.request.StockRequest;
import com.warehouse.dto.request.VariantRequest;
import com.warehouse.dto.response.VariantResponse;
import com.warehouse.entity.Item;
import com.warehouse.entity.Variant;
import com.warehouse.exception.OutOfStockException;
import com.warehouse.exception.ResourceNotFoundException;
import com.warehouse.repository.ItemRepository;
import com.warehouse.repository.VariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// logika bisnis untuk Variant
@Service
@RequiredArgsConstructor
@Transactional
public class VariantService {

    private final ItemRepository itemRepository;
    private final VariantRepository variantRepository;

    // ambil semua varian milik satu item
    @Transactional(readOnly = true)
    public List<VariantResponse> findByItemId(Long itemId) {
        getItemOrThrow(itemId);
        return variantRepository.findByItemId(itemId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // tambah varian baru ke item
    public VariantResponse create(Long itemId, VariantRequest request) {
        Item item = getItemOrThrow(itemId);
        Variant variant = Variant.builder()
                .item(item)
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        return toResponse(variantRepository.save(variant));
    }

    // update data varian
    public VariantResponse update(Long itemId, Long variantId, VariantRequest request) {
        getItemOrThrow(itemId);
        Variant variant = getVariantOrThrow(variantId);
        variant.setName(request.getName());
        variant.setPrice(request.getPrice());
        variant.setStock(request.getStock());
        return toResponse(variantRepository.save(variant));
    }

    // hapus varian
    public void delete(Long itemId, Long variantId) {
        getItemOrThrow(itemId);
        variantRepository.delete(getVariantOrThrow(variantId));
    }

    // set stok varian secara manual
    public VariantResponse updateStock(Long itemId, Long variantId, StockRequest request) {
        getItemOrThrow(itemId);
        Variant variant = getVariantOrThrow(variantId);
        variant.setStock(request.getStock());
        return toResponse(variantRepository.save(variant));
    }

    // proses penjualan varian, kurangi stok
    public VariantResponse sell(Long itemId, Long variantId, SellRequest request) {
        getItemOrThrow(itemId);
        Variant variant = getVariantOrThrow(variantId);

        // tolak jika stok tidak cukup
        if (variant.getStock() < request.getQuantity()) {
            throw new OutOfStockException(
                    "Stok varian tidak cukup. Stok tersedia: " + variant.getStock() + ", diminta: " + request.getQuantity()
            );
        }

        variant.setStock(variant.getStock() - request.getQuantity());
        return toResponse(variantRepository.save(variant));
    }

    // ambil item dari database, lempar error jika tidak ada
    private Item getItemOrThrow(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item dengan id " + id + " tidak ditemukan"));
    }

    // ambil varian dari database, lempar error jika tidak ada
    private Variant getVariantOrThrow(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Varian dengan id " + id + " tidak ditemukan"));
    }

    // ubah entity Variant menjadi response DTO
    private VariantResponse toResponse(Variant v) {
        return VariantResponse.builder()
                .id(v.getId())
                .itemId(v.getItem().getId())
                .name(v.getName())
                // jika varian tidak punya harga sendiri, pakai harga item induk
                .price(v.getPrice() != null ? v.getPrice() : v.getItem().getPrice())
                .stock(v.getStock())
                .build();
    }
}
