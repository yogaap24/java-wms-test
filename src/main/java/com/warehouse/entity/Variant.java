package com.warehouse.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// tabel variants di database
@Entity
@Table(name = "variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relasi ke item pemilik varian ini
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    // nama varian, misal: "Merah - L", "Size 42"
    @Column(nullable = false)
    private String name;

    // harga khusus varian; null = pakai harga dari item induk
    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    // stok varian ini
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;
}
