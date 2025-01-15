package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.TokenService;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.api.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "테스트용 API - /api/test ")

public class TokenTestController {
    private final TokenService tokenService;

    @Operation(summary = "테스트용 토큰 임의 발급",
            description = """
                     개발 편의를 위한 테스트 계정 토큰 발급 api - **Swagger에서만 사용**
                                        \s
                     memberId 값에 따라 토큰 발급 (1 ~ 5)
                    \s""")
    @GetMapping("/token")
    public ApiResponse<TokenResponse> testAccessToken(@RequestParam Long memberId){
        return ApiResponse.onSuccess(tokenService.createToken(memberId));
    }
}
