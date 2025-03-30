package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.MonthlyReport;
import com.easystock.backend.infrastructure.database.entity.enums.InvestmentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlyReportConverter {
    public static MonthlyReport toMonthlyReport(Member member, int year, int month,
                                                InvestmentType investmentType,
                                                String topStocksJson,
                                                String profitGraphJson) {
        return MonthlyReport.builder()
                .member(member)
                .year(year)
                .month(month)
                .investmentType(investmentType)
                .topStocksJson(topStocksJson)
                .profitGraphJson(profitGraphJson)
                .build();
    }
}
