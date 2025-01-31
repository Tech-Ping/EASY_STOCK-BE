package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.trade.TradeService;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.presentation.api.dto.request.TradeRequest;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResultResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/trades")
@Tag(name = "주문 API - /api/trades")
public class TradeController {

    private final TradeService tradeService;

    @GetMapping
    @Operation(
            summary = "거래 목록 조회 API - 회원의 거래 상태에 따른 거래 목록을 반환합니다. (입력 받은 상태가 없을 시 모든 거래 목록 반환)",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<List<TradeResponse>> getAllTrades(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @RequestParam(required = false) TradeStatus status) {
        if(status == null) return ApiResponse.onSuccess(tradeService.getAllTradesByUser(memberId));
        return ApiResponse.onSuccess(tradeService.getTradesByStatus(memberId, status));
    }

    @PostMapping
    @Operation(
            summary = "주식 거래 API - 매수 또는 매도 거래를 제출합니다. (예상되는 거래 상태: 대기 or 완료)",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<TradeResultResponse> stockTrade(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @RequestBody TradeRequest request) {
        return ApiResponse.onSuccess(tradeService.createTrade(memberId, request));
    }
}
