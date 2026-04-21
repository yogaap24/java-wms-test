# Warehouse Management System API

REST API untuk sistem manajemen gudang toko, dibuat dengan Java 17 dan Spring Boot 3.

---

## Cara Menjalankan Aplikasi

### Clone & Masuk ke Direktori

```bash
cd /Users/kindharika/Code/java-be-test
```

### Jalankan Aplikasi

```bash
mvn spring-boot:run
```

Pertama kali dijalankan, Maven akan download dependency dulu (butuh internet, ~1-2 menit).
Setelah itu muncul log seperti ini, artinya sudah berjalan:

```
Started WarehouseApplication in 3.2 seconds
```

### 1. Arsitektur Berlapis (Layered Architecture)

```
Controller → Service → Repository → Database
```

Saya pilih arsitektur ini karena:

- **Paling sederhana** dan mudah dipahami, tidak perlu Domain-Driven Design atau Hexagonal untuk scope kecil seperti ini
- Setiap lapisan punya tanggung jawab jelas: Controller terima request, Service urus logika bisnis, Repository urus database
- Mudah di-debug: jika ada error di logika bisnis, langsung cari di Service

### 2. H2 In-Memory Database

- Tidak perlu install PostgreSQL atau MySQL
- Cocok untuk demo/test karena tinggal `mvn spring-boot:run` langsung jalan
- Tradeoff: data hilang saat app restart — ini asumsi yang disepakati untuk keperluan test ini

### 3. Dua Model Stok: Item langsung vs Varian

- **Item tanpa varian**: stok disimpan di field `Item.stock`
- **Item dengan varian**: stok disimpan di masing-masing `Variant.stock`, `Item.stock = 0`
- Keputusan diambil karena tidak perlu tabel stok terpisah

### 4. Harga Varian Opsional

- Jika `Variant.price = null`, sistem otomatis pakai harga dari item induk
- Berguna untuk varian yang harganya sama (misal kaos ukuran S dan M sama-sama Rp 75.000)

### 5. Global Exception Handler

- Semua error ditangkap di satu tempat (`GlobalExceptionHandler`)
- Response selalu konsisten dalam format `ApiResponse<T>` dengan field `success`, `message`, `data`

### 6. Lombok

- Mengurangi kode boilerplate (getter, setter, constructor, builder)
- Tanpa Lombok, setiap entity/DTO butuh lebih banyak baris kode.

---

## Asumsi yang Dibuat

1. **Satu item bisa punya 0 atau banyak varian** — jika ada varian, penjualan harus melalui endpoint varian (bukan endpoint item langsung)
2. **Stok tidak bisa negatif** — jika request sell melebihi stok, API mengembalikan error 409 Conflict
3. **Data tidak persisten** — menggunakan H2 in-memory, data hilang saat app restart (acceptable untuk kebutuhan test)
4. **Tidak ada autentikasi** — tidak ada login/JWT karena scope test hanya CRUD + stock management
5. **Harga dalam format desimal** — menggunakan `BigDecimal` untuk mencegah masalah floating point pada kalkulasi uang
6. **Hapus item = hapus semua variannya** — menggunakan `CascadeType.ALL` + `orphanRemoval = true`

---

### Akses API

- **Base URL**: `http://localhost:8080`
- **H2 Console** (lihat isi database): `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:warehousedb`
  - Username: `sa`
  - Password: *(kosong)*

> **Catatan**: Data contoh sudah otomatis dimasukkan saat app start via `data.sql`. Data akan hilang saat app dimatikan karena pakai H2 in-memory.

---

## Daftar Semua Endpoint API

**Base URL**: `http://localhost:8080`

### Item Endpoints
| Method | Endpoint | Fungsi |
|--------|----------|--------|
| GET | `/api/items` | Ambil semua item |
| POST | `/api/items` | Buat item baru |
| GET | `/api/items/{id}` | Ambil satu item |
| PUT | `/api/items/{id}` | Update item |
| DELETE | `/api/items/{id}` | Hapus item |
| PUT | `/api/items/{id}/stock` | Update stok item |
| POST | `/api/items/{id}/sell` | Jual item (kurangi stok) |

### Variant Endpoints
| Method | Endpoint | Fungsi |
|--------|----------|--------|
| GET | `/api/items/{itemId}/variants` | Ambil semua varian |
| POST | `/api/items/{itemId}/variants` | Tambah varian baru |
| PUT | `/api/items/{itemId}/variants/{variantId}` | Update varian |
| DELETE | `/api/items/{itemId}/variants/{variantId}` | Hapus varian |
| PUT | `/api/items/{itemId}/variants/{variantId}/stock` | Update stok varian |
| POST | `/api/items/{itemId}/variants/{variantId}/sell` | Jual varian |

---

## Contoh API Endpoint

### Format Response Standar

Semua response mengikuti format:

```json
{
  "success": true,
  "message": "Pesan keterangan",
  "data": { ... }
}
```

---

### Item — CRUD

#### Ambil Semua Item

```
GET /api/items
```

Response:

```json
{
  "success": true,
  "message": "Berhasil mengambil semua item",
  "data": [
    {
      "id": 1,
      "name": "Pensil Staedtler",
      "description": "Pensil 2B berkualitas tinggi",
      "price": 5000,
      "stock": 100,
      "variants": []
    },
    {
      "id": 4,
      "name": "Kaos Polos Cotton Combed",
      "description": "Kaos cotton combed 30s",
      "price": 75000,
      "stock": 0,
      "variants": [
        { "id": 1, "itemId": 4, "name": "Putih - S", "price": 75000, "stock": 20 },
        { "id": 4, "itemId": 4, "name": "Hitam - S", "price": 80000, "stock": 8 }
      ]
    }
  ]
}
```

#### Buat Item Baru

```
POST /api/items
Content-Type: application/json

{
  "name": "Pulpen Pilot",
  "description": "Pulpen hitam 0.5mm",
  "price": 7500,
  "stock": 150
}
```

Response (201 Created):

```json
{
  "success": true,
  "message": "Item berhasil dibuat",
  "data": {
    "id": 6,
    "name": "Pulpen Pilot",
    "description": "Pulpen hitam 0.5mm",
    "price": 7500,
    "stock": 150,
    "variants": []
  }
}
```

#### Update Item

```
PUT /api/items/6
Content-Type: application/json

{
  "name": "Pulpen Pilot G2",
  "description": "Pulpen gel hitam 0.5mm",
  "price": 9000,
  "stock": 150
}
```

#### Hapus Item

```
DELETE /api/items/6
```

---

### Stok — Update & Jual

#### Update Stok Item (Restock)

```
PUT /api/items/1/stock
Content-Type: application/json

{
  "stock": 200
}
```

#### Jual Item (tanpa varian)

```
POST /api/items/1/sell
Content-Type: application/json

{
  "quantity": 5
}
```

Response sukses:

```json
{
  "success": true,
  "message": "Penjualan berhasil",
  "data": {
    "id": 1,
    "name": "Pensil Staedtler",
    "stock": 95,
    "variants": []
  }
}
```

Response jika stok tidak cukup (409 Conflict):

```json
{
  "success": false,
  "message": "Stok tidak cukup. Stok tersedia: 95, diminta: 200",
  "data": null
}
```

---

### Varian — CRUD + Stok + Jual

#### Ambil Semua Varian Item

```
GET /api/items/4/variants
```

#### Tambah Varian ke Item

```
POST /api/items/4/variants
Content-Type: application/json

{
  "name": "Navy - XL",
  "price": 85000,
  "stock": 15
}
```

> Jika `price` tidak diisi, otomatis pakai harga item induk (Rp 75.000)

#### Update Varian

```
PUT /api/items/4/variants/1
Content-Type: application/json

{
  "name": "Putih - S (Updated)",
  "stock": 25
}
```

#### Hapus Varian

```
DELETE /api/items/4/variants/1
```

#### Update Stok Varian

```
PUT /api/items/4/variants/2/stock
Content-Type: application/json

{
  "stock": 30
}
```

#### Jual Varian

```
POST /api/items/4/variants/2/sell
Content-Type: application/json

{
  "quantity": 3
}
```

Response sukses:

```json
{
  "success": true,
  "message": "Penjualan varian berhasil",
  "data": {
    "id": 2,
    "itemId": 4,
    "name": "Putih - M",
    "price": 75000,
    "stock": 12
  }
}
```

---

### Error — Contoh Response Error

#### Validasi Gagal (400)

```
POST /api/items
{ "description": "tanpa nama dan harga" }
```

```json
{
  "success": false,
  "message": "Validasi gagal",
  "data": {
    "name": "Nama item tidak boleh kosong",
    "price": "Harga tidak boleh kosong"
  }
}
```

#### Data Tidak Ditemukan (404)

```
GET /api/items/999
```

```json
{
  "success": false,
  "message": "Item dengan id 999 tidak ditemukan",
  "data": null
}
```

---

## Struktur Proyek

```
src/main/java/com/warehouse/
├── WarehouseApplication.java       ← titik masuk aplikasi
├── controller/
│   ├── ItemController.java         ← endpoint /api/items
│   └── VariantController.java      ← endpoint /api/items/{id}/variants
├── service/
│   ├── ItemService.java            ← logika bisnis item
│   └── VariantService.java         ← logika bisnis varian
├── repository/
│   ├── ItemRepository.java         ← query database item
│   └── VariantRepository.java      ← query database varian
├── entity/
│   ├── Item.java                   ← tabel items
│   └── Variant.java                ← tabel variants
├── dto/
│   ├── request/                    ← data masuk dari client
│   └── response/                   ← data keluar ke client
└── exception/
    ├── GlobalExceptionHandler.java ← tangani semua error
    ├── OutOfStockException.java    ← stok habis
    └── ResourceNotFoundException.java ← data tidak ada
src/main/resources/
├── application.properties          ← konfigurasi app & database
└── data.sql                        ← data contoh otomatis
```