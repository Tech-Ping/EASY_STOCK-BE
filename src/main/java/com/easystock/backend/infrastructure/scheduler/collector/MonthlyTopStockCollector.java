package com.easystock.backend.infrastructure.scheduler.collector;

import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.StockRecord;
import com.easystock.backend.infrastructure.database.repository.InventoryRepository;
import com.easystock.backend.infrastructure.database.repository.StockRecordRepository;
import com.easystock.backend.presentation.api.dto.converter.StockRecordConverter;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;
import com.easystock.backend.util.DateUtils;
import com.easystock.backend.util.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MonthlyTopStockCollector {

    private final InventoryRepository inventoryRepository;
    private final StockRecordRepository stockRecordRepository;

    public List<MonthlyStockInfoResponse> selectTopStock(Member member) {
        List<Inventory> inventories = inventoryRepository.findAllByMember(member);
        LocalDate oneMonthAgoStd = DateUtils.getValidOneMonthAgo();

        int MAX_SIZE = 5;
        return inventories.stream()
                .sorted(Comparator.comparingInt(inv -> -FormatUtils.calculateTotalInvestedAmount(inv)))
                .limit(MAX_SIZE)
                .map(inv -> {
                    String code = inv.getStock().getCode();
                    String name = inv.getStock().getName();
                    Integer lastClosePrice = null;
                    Integer lastMonthClosePrice = null;

                    // 1. 현재(전날) 종가
                    int currentPrice = stockRecordRepository
                            .findTopByStockCodeAndDateBeforeOrderByDateDesc(code, LocalDate.now())
                            .map(StockRecord::getClosePrice)
                            .orElse(lastClosePrice != 0 ? lastClosePrice : 0);

                    if (currentPrice != 0) lastClosePrice = currentPrice;

                    // 2. 한달 전 종가
                    int oneMonthAgoPrice = stockRecordRepository
                            .findTopByStockCodeAndDateBeforeOrderByDateDesc(code, DateUtils.getValidOneMonthAgo())
                            .map(StockRecord::getClosePrice)
                            .orElse(lastMonthClosePrice != null ? lastMonthClosePrice : 0);

                    if (oneMonthAgoPrice != 0) lastMonthClosePrice = oneMonthAgoPrice;

                    Double rate = FormatUtils.calculateChangeRate(currentPrice, oneMonthAgoPrice);
                    return StockRecordConverter.toMonthlyStockInfoResponse(code, name, currentPrice, rate);
                })
                .collect(Collectors.toList());
    }
}
