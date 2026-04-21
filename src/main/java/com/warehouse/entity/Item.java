package com.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// tabel items di database
@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nama produk, wajib diisi
    @Column(nullable = false)
    private String name;

    // deskripsi produk, boleh kosong
    private String description;

    // harga dasar produk
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    // stok item jika tidak punya varian
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    // relasi ke tabel variants (satu item bisa punya banyak varian)
    // cascade: hapus item = hapus semua variannya
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Variant> variants = new ArrayList<>();
}
