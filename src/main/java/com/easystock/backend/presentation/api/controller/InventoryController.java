package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.auth.AuthService;
import com.easystock.backend.application.service.inventory.InventoryService;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.presentation.api.dto.response.InventoryResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventories")
@Tag(name = "인벤토리(사용자 보유 주식) API - /api/inventories ")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @Operation(
            summary = "보유 주식 목록 조회 API - 로그인한 회원의 보유 주식 목록을 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    public ApiResponse<List<InventoryResponse>> getInventories(
            @Parameter(hidden = true)
            @AuthUser Long memberId) {
        return ApiResponse.onSuccess(inventoryService.getInventories(memberId));
    }
}
