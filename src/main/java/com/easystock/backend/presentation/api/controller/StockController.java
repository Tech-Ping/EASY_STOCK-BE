package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.stock.StockService;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
