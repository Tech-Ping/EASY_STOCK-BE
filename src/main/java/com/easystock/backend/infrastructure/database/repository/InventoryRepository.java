package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
