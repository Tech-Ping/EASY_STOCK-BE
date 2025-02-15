package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.level.LevelService;
import com.easystock.backend.presentation.api.dto.response.LevelUpResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/level")
@Tag(name = "레벨 API - /api/level ")
public class LevelController {
    private final LevelService levelService;
    @PostMapping("/up")
    @Operation(
            summary = "회원 레벨업 API - 회원이 레벨업을 요청합니다.",
            description = "회원이 레벨업을 요청합니다. \s" +
                    "해당 레벨의 튜토리얼과 필수 퀴즈를 다 풀고, 경험치 기준을 충족하면 레벨업이 진행됩니다. 레벨업 성공 시, 보상으로 3000STOKEN을 받습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<LevelUpResponse> levelUp(
            @Parameter(hidden = true)
            @AuthUser Long memberId){
        return ApiResponse.onSuccess(levelService.levelUpIfPossible(memberId));
    }

}
