package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.StockRecord;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;
import com.easystock.backend.util.FormatUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StockRecordConverter {
    public static StockRecord toRecord(
            String stockCode, LocalDate date, Integer closePrice
    ){
        return StockRecord.builder()
                .stockCode(stockCode)
                .date(date)
                .closePrice(closePrice)
                .build();
    }

    public static MonthlyStockInfoResponse toMonthlyStockInfoResponse(
            String stockCode, String stockName, int currentPrice, Double lastMonthChangeRate
    ) {
        return MonthlyStockInfoResponse.builder()
                .stockCode(stockCode)
                .stockName(stockName)
                .currentPrice(currentPrice)
                .lastMonthChangeRate(FormatUtils.formatRatePercentage(lastMonthChangeRate))
                .build();
    }
}
