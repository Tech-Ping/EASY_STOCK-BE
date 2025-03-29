package com.easystock.backend.presentation.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MonthlyStockInfoResponse {
    @Schema(description = "종목 코드", example = "005930")
    private String stockCode;     // 종목 코드

    @Schema(description = "종목 이름", example = "삼성전자")
    private String stockName;     // 종목 이름

    @Schema(description = "현재 가격", example = "3000")
    private int currentPrice;

    @Schema(description = "저번 달 대비 변화율 (%)", example = "10.3")
    private String lastMonthChangeRate;
}
