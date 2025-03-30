package com.easystock.backend.presentation.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MonthlyReportResponse {
    @Schema(description = "연월 정보", example = "2025년 3월")
    private String reportDate;

    @Schema(description = "이번 달 투자 유형", example = "적극 투자형")
    private InvestmentTypeInfo investmentType;

    @Schema(description = "가장 많이 투자한 주식 현황 Top 5", example = "삼성전자의 주식 현황")
    private List<MonthlyStockInfoResponse> topStocks;

    @Schema(description = "총 투자금 - 실 수익 변동 그래프")
    private List<DailyProfit> profitGraph;
}
