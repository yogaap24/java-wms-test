-- Data contoh untuk testing (otomatis dimasukkan saat aplikasi start)

-- Item tanpa varian (stok langsung di item)
INSERT INTO items (name, description, price, stock) VALUES ('Pensil Staedtler', 'Pensil 2B berkualitas tinggi', 5000, 100);
INSERT INTO items (name, description, price, stock) VALUES ('Buku Tulis 40 Lembar', 'Buku tulis polos ukuran A5', 8000, 50);
INSERT INTO items (name, description, price, stock) VALUES ('Penghapus Faber-Castell', 'Penghapus bersih tanpa residu', 3500, 200);

-- Item dengan varian (stok ada di varian, bukan di item)
INSERT INTO items (name, description, price, stock) VALUES ('Kaos Polos Cotton Combed', 'Kaos cotton combed 30s, berbagai warna dan ukuran', 75000, 0);
INSERT INTO items (name, description, price, stock) VALUES ('Sepatu Sneakers Casual', 'Sepatu kasual pria/wanita, tersedia berbagai ukuran', 250000, 0);

-- Varian untuk Kaos Polos (item id=4)
INSERT INTO variants (item_id, name, price, stock) VALUES (4, 'Putih - S', NULL, 20);
INSERT INTO variants (item_id, name, price, stock) VALUES (4, 'Putih - M', NULL, 15);
INSERT INTO variants (item_id, name, price, stock) VALUES (4, 'Putih - L', NULL, 10);
INSERT INTO variants (item_id, name, price, stock) VALUES (4, 'Hitam - S', 80000, 8);
INSERT INTO variants (item_id, name, price, stock) VALUES (4, 'Hitam - M', 80000, 5);
INSERT INTO variants (item_id, name, price, stock) VALUES (4, 'Hitam - L', 80000, 0);

-- Varian untuk Sepatu Sneakers (item id=5)
INSERT INTO variants (item_id, name, price, stock) VALUES (5, 'Size 39', NULL, 6);
INSERT INTO variants (item_id, name, price, stock) VALUES (5, 'Size 40', NULL, 12);
INSERT INTO variants (item_id, name, price, stock) VALUES (5, 'Size 41', NULL, 8);
INSERT INTO variants (item_id, name, price, stock) VALUES (5, 'Size 42', NULL, 4);
INSERT INTO variants (item_id, name, price, stock) VALUES (5, 'Size 43', 265000, 2);
INSERT INTO variants (item_id, name, price, stock) VALUES (5, 'Size 44', 265000, 0);
