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
@Schema(description = "투자자별 주식 순매수 정보 Response")
public class StockAmountResponse {

    @Schema(description = "주식 영업 일자", example = "20250405")
    private String date;

    @Schema(description = "외국인 순매수 거래 대금", example = "-425869")
    private String foreignAmounts;

    @Schema(description = "개인 순매수 거래 대금", example = "-964779")
    private String personAmounts;

    @Schema(description = "기관계 순매수 거래 대금", example = "4353455")
    private String organizationAmounts;
}
