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
@Schema(description = "주식별 기업 정보 Response")
public class StockInfoResponse {

    @Schema(description = "주식명", example = "삼성전자")
    private String stockName;

    @Schema(description = "시장구분", example = "KOSPI")
    private String marketType;

    @Schema(description = "업종 한글 종목명", example = "전기 전자")
    private String sectorName;

    @Schema(description = "시가총액", example = "964779")
    private String marketCap;

    @Schema(description = "상장주식수", example = "4353455")
    private String listedShares;

    @Schema(description = "자본금", example = "4353455")
    private String capital;

    @Schema(description = "액면가", example = "4353455")
    private String parValue;
}
