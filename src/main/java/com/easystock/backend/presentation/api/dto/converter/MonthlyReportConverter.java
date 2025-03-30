package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.MonthlyReport;
import com.easystock.backend.infrastructure.database.entity.enums.InvestmentType;
import com.easystock.backend.presentation.api.dto.response.DailyProfit;
import com.easystock.backend.presentation.api.dto.response.InvestmentTypeInfo;
import com.easystock.backend.presentation.api.dto.response.MonthlyReportResponse;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;
import com.easystock.backend.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonthlyReportConverter {
    public static MonthlyReport toMonthlyReport(Member member, int year, int month,
                                                InvestmentType investmentType,
                                                String profitGraphJson) {
        return MonthlyReport.builder()
                .member(member)
                .year(year)
                .month(month)
                .investmentType(investmentType)
                .profitGraphJson(profitGraphJson)
                .build();
    }

    public static MonthlyReportResponse toMonthlyReportResponse(
            YearMonth targetMonth,
            InvestmentType investmentType,
            List<MonthlyStockInfoResponse> topStocks,
            List<DailyProfit> profitGraph
    ) {
        return MonthlyReportResponse.builder()
                .reportDate(DateUtils.formatYearMonth(targetMonth))
                .investmentType(InvestmentTypeInfo.from(investmentType))
                .topStocks(topStocks)
                .profitGraph(profitGraph)
                .build();
    }
}
