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
            // 종목별 누적 수량, 누적 투자금
            Map<String, Integer> quantityMap = new HashMap<>();
            Map<String, Integer> investedMap = new HashMap<>();

            for (Inventory inventory : inventories) {
                if (inventory.getCreatedAt().toLocalDate().isAfter(date)) continue;

                String stockCode = inventory.getStock().getCode();
                int quantity = inventory.getQuantity();
                int totalPrice = inventory.getTotalPrice();

                quantityMap.merge(stockCode, quantity, Integer::sum);
                investedMap.merge(stockCode, totalPrice, Integer::sum);
            }

            int totalInvested = 0;
            int totalEvaluated = 0;

            for (String stockCode : quantityMap.keySet()) {
                int quantity = quantityMap.get(stockCode);
                int invested = investedMap.get(stockCode);

                int closePrice = stockRecordRepository
                        .findFirstByStockCodeAndDateLessThanEqualOrderByDateDesc(stockCode, date)
                        .map(StockRecord::getClosePrice)
                        .orElse(lastClosePriceMap.getOrDefault(stockCode, 0));

                if (closePrice != 0) {
                    lastClosePriceMap.put(stockCode, closePrice);
                }

                totalInvested += invested;
                totalEvaluated += closePrice * quantity;
            }

            int realProfit = totalEvaluated - totalInvested;

            result.add(DailyProfit.from(
                    date,
                    totalInvested,
                    realProfit,
                    totalEvaluated,
                    totalInvested
            ));
        }

        return result;
    }
}
