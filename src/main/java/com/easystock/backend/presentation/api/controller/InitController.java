package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.stock.StockInfoInitializeService;
import com.easystock.backend.application.service.token.TokenService;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.api.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "테스트용 API - /api/test ")
@RequestMapping("/api/test/")
public class InitController {

    private final TokenService tokenService;
    private final StockInfoInitializeService stockInitService;

    @PostMapping("/stock-info/initialize")
    @Operation(
            summary = "종가 정보 초기화 API - 최근 1달 동안의 주식 종가 정보를 DB에 저장합니다."
    )
    public ApiResponse<Boolean> initializeStockInfo(){
        return ApiResponse.onSuccess(stockInitService.fillInitialStockRecords());
    }

    @GetMapping("/token")
    @Operation(summary = "테스트용 토큰 임의 발급",
            description = """
                     개발 편의를 위한 테스트 계정 토큰 발급 api - **Swagger에서만 사용**
                                        \s
                     memberId 값에 따라 토큰 발급 (1 ~ 5)
                    \s""")
    public ApiResponse<TokenResponse> testAccessToken(@RequestParam Long memberId){
        return ApiResponse.onSuccess(tokenService.createToken(memberId));
    }
}
