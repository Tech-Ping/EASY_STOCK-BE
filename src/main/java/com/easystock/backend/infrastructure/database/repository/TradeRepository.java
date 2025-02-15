package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findAllByCustomer(Member customer);
    List<Trade> findAllByCustomerAndStatus(Member customer, TradeStatus status);
    List<Trade> findTradesByStatusAndStock(TradeStatus status, Stock stock);
}
