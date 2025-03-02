package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.infrastructure.database.entity.enums.MarketType;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Builder
@ToString
@Schema(description = "주식 호가/잔량 Response")
public class StockQuotesResponse {

    @Schema(description = "매도/매수 구분", example = "BUY")
    private TradeType tradeType; // 매도, 매수

    @Schema(description = "주식 이름", example = "삼성전자")
    private String stockName; // 주식 이름

    @Schema(description = "현재가", example = "65000")
    private String marketPrice; // 현재가

    @Schema(description = "호가1", example = "65500")
    private String ask1; // 호가1

    @Schema(description = "호가2", example = "65600")
    private String ask2; // 호가2

    @Schema(description = "호가3", example = "65700")
    private String ask3; // 호가3

    @Schema(description = "호가4", example = "65800")
    private String ask4; // 호가4

    @Schema(description = "호가5", example = "65900")
    private String ask5; // 호가5

    @Schema(description = "호가6", example = "66000")
    private String ask6; // 호가6

    @Schema(description = "호가7", example = "66100")
    private String ask7; // 호가7

    @Schema(description = "호가8", example = "66200")
    private String ask8; // 호가8

    @Schema(description = "호가9", example = "66300")
    private String ask9; // 호가9

    @Schema(description = "호가10", example = "66400")
    private String ask10; // 호가10

    @Schema(description = "잔량1", example = "1000")
    private String quantity1; // 잔량1

    @Schema(description = "잔량2", example = "2000")
    private String quantity2; // 잔량2

    @Schema(description = "잔량3", example = "3000")
    private String quantity3; // 잔량3

    @Schema(description = "잔량4", example = "4000")
    private String quantity4; // 잔량4

    @Schema(description = "잔량5", example = "5000")
    private String quantity5; // 잔량5

    @Schema(description = "잔량6", example = "6000")
    private String quantity6; // 잔량6

    @Schema(description = "잔량7", example = "7000")
    private String quantity7; // 잔량7

    @Schema(description = "잔량8", example = "8000")
    private String quantity8; // 잔량8

    @Schema(description = "잔량9", example = "9000")
    private String quantity9; // 잔량9

    @Schema(description = "잔량10", example = "10000")
    private String quantity10; // 잔량10
}
