package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
