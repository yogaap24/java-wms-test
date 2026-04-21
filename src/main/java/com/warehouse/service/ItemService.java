package com.warehouse.service;

import com.warehouse.dto.request.ItemRequest;
import com.warehouse.dto.request.SellRequest;
import com.warehouse.dto.request.StockRequest;
import com.warehouse.dto.response.ItemResponse;
import com.warehouse.dto.response.VariantResponse;
import com.warehouse.entity.Item;
import com.warehouse.entity.Variant;
import com.warehouse.exception.OutOfStockException;
import com.warehouse.exception.ResourceNotFoundException;
import com.warehouse.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// logika bisnis untuk Item
@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    // ambil semua item beserta variannya
    @Transactional(readOnly = true)
    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ambil satu item berdasarkan id
    @Transactional(readOnly = true)
    public ItemResponse findById(Long id) {
        return toResponse(getItemOrThrow(id));
    }

    // buat item baru
    public ItemResponse create(ItemRequest request) {
        Item item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();
        return toResponse(itemRepository.save(item));
    }

    // update data item
    public ItemResponse update(Long id, ItemRequest request) {
        Item item = getItemOrThrow(id);
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setStock(request.getStock());
        return toResponse(itemRepository.save(item));
    }

    // hapus item (varian ikut terhapus karena cascade)
    public void delete(Long id) {
        itemRepository.delete(getItemOrThrow(id));
    }

    // set stok item secara manual (misalnya saat restock)
    public ItemResponse updateStock(Long id, StockRequest request) {
        Item item = getItemOrThrow(id);
        item.setStock(request.getStock());
        return toResponse(itemRepository.save(item));
    }

    // proses penjualan item (tanpa varian), kurangi stok
    public ItemResponse sell(Long id, SellRequest request) {
        Item item = getItemOrThrow(id);

        // jika item punya varian, penjualan harus melalui endpoint varian
        if (!item.getVariants().isEmpty()) {
            throw new IllegalStateException("Item ini memiliki varian. Gunakan endpoint /variants/{variantId}/sell");
        }

        // tolak jika stok tidak cukup
        if (item.getStock() < request.getQuantity()) {
            throw new OutOfStockException(
                    "Stok tidak cukup. Stok tersedia: " + item.getStock() + ", diminta: " + request.getQuantity()
            );
        }

        item.setStock(item.getStock() - request.getQuantity());
        return toResponse(itemRepository.save(item));
    }

    // ambil item dari database, lempar error jika tidak ada
    private Item getItemOrThrow(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item dengan id " + id + " tidak ditemukan"));
    }

    // ubah entity Item menjadi response DTO
    private ItemResponse toResponse(Item item) {
        List<VariantResponse> variantResponses = item.getVariants().stream()
                .map(v -> toVariantResponse(v, item))
                .collect(Collectors.toList());

        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .stock(item.getStock())
                .variants(variantResponses)
                .build();
    }

    // ubah entity Variant menjadi response DTO
    private VariantResponse toVariantResponse(Variant v, Item item) {
        return VariantResponse.builder()
                .id(v.getId())
                .itemId(item.getId())
                .name(v.getName())
                // jika varian tidak punya harga sendiri, pakai harga item
                .price(v.getPrice() != null ? v.getPrice() : item.getPrice())
                .stock(v.getStock())
                .build();
    }
}
