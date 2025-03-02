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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            summary = "주식 거래 API - 매수 또는 매도 거래를 생성합니다. (예상되는 거래 상태: 대기 or 완료)",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<TradeResultResponse> stockTrade(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @RequestBody TradeRequest request) {
        return ApiResponse.onSuccess(tradeService.createTrade(memberId, request));
    }

    @PatchMapping("/{tradeId}/cancel")
    @Operation(
            summary = "거래 대기 취소 API - 미체결된 거래(대기 중인 거래)를 취소합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<TradeResultResponse> cancelTrade(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @PathVariable Long tradeId) {
        return ApiResponse.onSuccess(tradeService.cancelTrade(memberId, tradeId));
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "상태별 거래 목록 조회 API - 로그인한 사용자의 특정 상태에 따른 거래 목록을 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<List<TradeResponse>> getTradesByStatus(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @PathVariable("status") TradeStatus status) {
        return ApiResponse.onSuccess(tradeService.getTradesByStatus(memberId, status));
    }

}
