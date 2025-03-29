package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.util.DateUtils;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailyProfit {

    private String date;
    private int totalTradeAmount;
    private int realProfit;

    public static DailyProfit from(LocalDate date, int totalInvestment, int realProfit) {
        return DailyProfit.builder()
                .date(DateUtils.formatDateWithDot(date))
                .totalTradeAmount(totalInvestment)
                .realProfit(realProfit)
                .build();
    }

}
