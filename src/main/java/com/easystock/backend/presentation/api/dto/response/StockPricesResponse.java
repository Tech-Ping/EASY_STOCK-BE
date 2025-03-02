package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.infrastructure.database.entity.enums.MarketType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "주식 정보 Response")
public class StockPricesResponse {

    @Schema(description = "고유 ID", example = "1")
    private Long id;

    @Schema(description = "종목 코드", example = "005930")
    private String stockCode;     // 종목 코드

    @Schema(description = "종목 이름", example = "삼성전자")
    private String stockName;     // 종목 이름

    @Schema(description = "종목 정보", example = "증30 담140 신10억")
    private String stockInfo;

    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String imgUrl;

    @Schema(description = "시장 타입", example = "KOSPI")
    private MarketType stockIndustry; // 종목 카테고리

    @Schema(description = "주식 현재가", example = "60000")
    private Long stckPrpr;      // 주식 현재가

    @Schema(description = "전일 대비 금액(등락)", example = "-500")
    private Long prdyVrss;      // 전일 대비

    @Schema(description = "전일 대비율(등락률) (%)", example = "-0.83")
    private Double prdyCtrt;      // 전일 대비율

    @Schema(description = "누적 거래 대금 (원)", example = "1200000000")
    private Long acmlTrPbmn;    // 누적 거래 대금

    @Schema(description = "누적 거래량 (주)", example = "30000")
    private Long acmlVol;       // 누적 거래량
}
