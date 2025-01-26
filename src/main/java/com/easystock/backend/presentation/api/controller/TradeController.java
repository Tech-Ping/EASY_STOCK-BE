package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.trade.TradeService;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
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
            summary = "모든 거래 목록 조회 API - 로그인한 사용자의 모든 거래를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<List<TradeResponse>> getAllTrades(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @RequestParam(required = false) TradeStatus status) {
        if(status == null) return ApiResponse.onSuccess(tradeService.getAllTradesByUser(memberId));
        return ApiResponse.onSuccess(tradeService.getTradesByStatus(memberId, status));
    }
}
