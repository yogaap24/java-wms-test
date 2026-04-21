package com.warehouse.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// konfigurasi tambahan aplikasi
@Configuration
public class AppConfig {

    // tampilkan list endpoint saat aplikasi startup
    @Bean
    public ApplicationRunner printEndpoints() {
        return args -> {
            System.out.println("\n" +
                    "╔════════════════════════════════════════════════════════════════════════════════╗\n" +
                    "║              Warehouse Management System API - Daftar Endpoint                 ║\n" +
                    "╠════════════════════════════════════════════════════════════════════════════════╣\n" +
                    "║                                                                                ║\n" +
                    "║  Base URL: http://localhost:8080                                             ║\n" +
                    "║                                                                                ║\n" +
                    "║  ITEM ENDPOINTS                                                             ║\n" +
                    "║    GET    /api/items                    → Ambil semua item                    ║\n" +
                    "║    POST   /api/items                    → Buat item baru                      ║\n" +
                    "║    GET    /api/items/{id}               → Ambil satu item                     ║\n" +
                    "║    PUT    /api/items/{id}               → Update item                         ║\n" +
                    "║    DELETE /api/items/{id}               → Hapus item                          ║\n" +
                    "║    PUT    /api/items/{id}/stock         → Update stok item                    ║\n" +
                    "║    POST   /api/items/{id}/sell          → Jual item                           ║\n" +
                    "║                                                                                ║\n" +
                    "║  VARIANT ENDPOINTS                                                          ║\n" +
                    "║    GET    /api/items/{itemId}/variants                    → Ambil semua varian ║\n" +
                    "║    POST   /api/items/{itemId}/variants                    → Tambah varian     ║\n" +
                    "║    PUT    /api/items/{itemId}/variants/{variantId}        → Update varian     ║\n" +
                    "║    DELETE /api/items/{itemId}/variants/{variantId}        → Hapus varian      ║\n" +
                    "║    PUT    /api/items/{itemId}/variants/{variantId}/stock  → Update stok varian║\n" +
                    "║    POST   /api/items/{itemId}/variants/{variantId}/sell   → Jual varian       ║\n" +
                    "║                                                                                ║\n" +
                    "║  DATABASE CONSOLE                                                           ║\n" +
                    "║    Buka: http://localhost:8080/h2-console                                    ║\n" +
                    "║    Username: sa                                                               ║\n" +
                    "║    Password: (kosong)                                                         ║\n" +
                    "║                                                                                ║\n" +
                    "╚════════════════════════════════════════════════════════════════════════════════╝\n");
        };
    }
}
