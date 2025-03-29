package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.util.DateUtils;
import com.easystock.backend.util.FormatUtils;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DailyProfit {

    private String date;
    private int totalTradeAmount;
    private int realProfit;
    private String realProfitRate;

    public static DailyProfit from(LocalDate date, int totalInvestment, int realProfit) {
        Double profitRate = totalInvestment == 0 ? null : (realProfit * 100.0) / totalInvestment;
        return DailyProfit.builder()
                .date(DateUtils.formatDateWithDot(date))
                .totalTradeAmount(totalInvestment)
                .realProfit(realProfit)
                .realProfitRate(FormatUtils.formatRatePercentage(profitRate))
                .build();
    }

}
