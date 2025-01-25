package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.stock.StockService;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import com.easystock.backend.presentation.api.dto.response.StockQuotesResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/stocks")
@Tag(name = "주식 API - /api/stocks")
public class StockController {

    private final StockService stockService;

    @GetMapping
    @Operation(
            summary = "주식 목록 조회하는 API - 주식들의 기본 정보 및 종목명, 현재가, 등락, 등락률 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<List<StockPricesResponse>> getStockPrices(){
        return ApiResponse.onSuccess(stockService.getStockPrices());
    }

    @GetMapping("/{stockId}")
    @Operation(
            summary = "주식 세부 정보 조회 API - 특정 주식의 기본 정보 및 종목명, 현재가, 등락, 등락률 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<StockPricesResponse> getStockPrice(
            @Parameter(description = "주식 ID", required = true)
            @PathVariable Long stockId) {
        return ApiResponse.onSuccess(stockService.getStockPrice(stockId));
    }

    @GetMapping("/{stockId}/quotes")
    @Operation(
            summary = "주식 호가/잔량 조회 API - 특정 주식의 매도/매수에 따른 이름, 현재가, 호가 10개, 잔량 10개 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<StockQuotesResponse> getStockQuotes(
            @Parameter(description = "주식 ID", required = true)
            @PathVariable Long stockId,
            @Parameter(description = "거래 타입 (SELL 또는 BUY)", required = true)
            @RequestParam(required = false) TradeType type
    ) {
        return ApiResponse.onSuccess(stockService.getStockQuotes(stockId, type));
    }
}
