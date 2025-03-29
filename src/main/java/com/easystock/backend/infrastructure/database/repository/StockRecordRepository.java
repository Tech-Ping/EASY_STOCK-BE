package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.StockRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface StockRecordRepository extends JpaRepository<StockRecord, Long> {
    boolean existsByStockCodeAndDate(String stockCode, LocalDate date);

    @Query("SELECT sr FROM StockRecord sr WHERE sr.stockCode = :stockCode AND sr.date <= :date ORDER BY sr.date DESC LIMIT 1")
    Optional<StockRecord> findRecentBefore(@Param("stockCode") String stockCode, @Param("date") LocalDate date);
}
