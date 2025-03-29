package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
}
