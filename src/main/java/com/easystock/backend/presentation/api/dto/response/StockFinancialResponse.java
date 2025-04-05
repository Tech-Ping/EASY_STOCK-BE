package com.easystock.backend.presentation.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "주식별 재무 정보 Response")
public class StockFinancialResponse {
    @Schema(description = "분기", example = "202309")
    private String quarter;

    @Schema(description = "매출액", example = "3343545")
    private String revenue;

    @Schema(description = "영업이익", example = "465454")
    private String operatingProfit;

    @Schema(description = "당기순이익", example = "543543543")
    private String netIncome;

    @Schema(description = "자산", example = "43534534")
    private String totalAssets;

    @Schema(description = "부채", example = "654645")
    private String totalLiabilities;

    @Schema(description = "자본", example = "643")
    private String equity;

    @Schema(description = "자본금", example = "4534")
    private String capitalStock;
}
