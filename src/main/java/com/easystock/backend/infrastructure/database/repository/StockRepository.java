package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
