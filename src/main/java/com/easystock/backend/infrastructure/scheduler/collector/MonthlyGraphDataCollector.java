package com.easystock.backend.infrastructure.scheduler.collector;

import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.StockRecord;
import com.easystock.backend.infrastructure.database.repository.InventoryRepository;
import com.easystock.backend.infrastructure.database.repository.StockRecordRepository;
import com.easystock.backend.presentation.api.dto.response.DailyProfit;
import com.easystock.backend.util.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MonthlyGraphDataCollector {
    private final StockRecordRepository stockRecordRepository;
    private final InventoryRepository inventoryRepository;

    public List<DailyProfit> generateDailyProfitGraph(Member member, LocalDate start, LocalDate end) {
        List<Inventory> inventories = inventoryRepository.findAllByMember(member);
        List<DailyProfit> result = new ArrayList<>();

        Map<String, Integer> lastClosePriceMap = new HashMap<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            int totalInvested = 0;
            int totalEvaluated = 0;

            for (Inventory inventory : inventories) {
                int quantity = inventory.getQuantity();
                int avgPurchasePrice = inventory.getTotalPrice() / quantity;
                String stockCode = inventory.getStock().getCode();

                int closePrice = stockRecordRepository
                        .findTopByStockCodeAndDateBeforeOrderByDateDesc(stockCode, date)
                        .map(StockRecord::getClosePrice)
                        .orElse(lastClosePriceMap.getOrDefault(stockCode, 0));

                if (closePrice != 0) {
                    lastClosePriceMap.put(stockCode, closePrice);
                }

                totalInvested += avgPurchasePrice * quantity;
                totalEvaluated += closePrice * quantity;
            }

            int realProfit = totalEvaluated - totalInvested;
            result.add(DailyProfit.from(date, totalInvested, realProfit, totalEvaluated, totalInvested));
        }
        return result;
    }
}
