package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.tutorial.TutorialService;
import com.easystock.backend.presentation.api.dto.response.CompleteTutorialResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/tutorials")
@Tag(name = "튜토리얼 API - /api/tutorials")
public class TutorialController {
    private final TutorialService tutorialService;

    @PostMapping("/complete")
    @Operation(
            summary = "튜토리얼 완료하는 API - 특정 레벨의 튜토리얼 완료 후, 보상으로 1000 STOKEN을 받습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<CompleteTutorialResponse> completeTutorial(
            @Parameter(hidden = true)
            @AuthUser Long memberId
    ){
        return ApiResponse.onSuccess(tutorialService.completeTutorial(memberId));
    }
}
