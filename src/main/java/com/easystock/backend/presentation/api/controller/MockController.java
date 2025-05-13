package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.mock.MockService;
import com.easystock.backend.presentation.api.dto.request.StokenRequest;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "mock API - /api ")
public class MockController {
    private final MockService mockService;

    @PostMapping("/stoken")
    @Operation(
            summary = "회원 STOKEN API -  mock API",
            description = "회원이 STOKEN을 요청합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<Void> earnStoken(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @RequestBody @Valid StokenRequest request
    ) {
        mockService.acquireStoken(memberId, request);
        return ApiResponse.onSuccess();
    }
}
