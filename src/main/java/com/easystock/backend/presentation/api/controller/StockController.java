package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.stock.StockService;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import com.easystock.backend.presentation.api.dto.response.*;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Optional;


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


    @GetMapping("/{stockId}/amounts")
    @Operation(
            summary = "투자자별 순매수 거래 대금 조회 API - 특정 주식의 8일간의 투자자별(외국인, 개인, 기관) 순매수 거래 대금 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<List<StockAmountResponse>> getStockAmounts(
            @Parameter(description = "주식 ID", required = true)
            @PathVariable Long stockId
    ) {
        return ApiResponse.onSuccess(stockService.getStockAmountFromApi(stockId));
    }

    @GetMapping("/{stockId}/infos")
    @Operation(
            summary = "주식별 기업 정보 조회 API - 특정 주식의 기업 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<Optional<StockInfoResponse>> getStockInfos(
            @Parameter(description = "주식 ID", required = true)
            @PathVariable Long stockId
    ) {
        return ApiResponse.onSuccess(stockService.getStockInfo(stockId));
    }

    @GetMapping("/{stockId}/financials")
    @Operation(
            summary = "주식별 재무 정보 조회 API - 특정 주식의 재무 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<List<StockFinancialResponse>> getFinancials(
            @Parameter(description = "주식 ID", required = true)
            @PathVariable Long stockId
    ) {
        return ApiResponse.onSuccess(stockService.getFinancials(stockId));
    }
}
