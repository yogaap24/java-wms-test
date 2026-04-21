package com.warehouse.repository;

import com.warehouse.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// akses database untuk tabel variants
@Repository
public interface VariantRepository extends JpaRepository<Variant, Long> {

    // cari semua varian milik item tertentu
    List<Variant> findByItemId(Long itemId);
}
