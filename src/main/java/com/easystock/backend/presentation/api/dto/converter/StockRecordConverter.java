package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.StockRecord;
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
}
