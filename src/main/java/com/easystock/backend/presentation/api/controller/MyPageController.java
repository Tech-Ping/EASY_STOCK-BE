package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.auth.MyPageService;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my")
@Tag(name = "유저 인증/인가 API - /api/my ")
public class MyPageController {
    private final MyPageService myPageService;
    @GetMapping("/profile")
    @Operation(
            summary = "유저 프로필 조회 API - 유저가 본인의 프로필을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<GetMemberProfileResponse> getMyProfile(
            @Parameter(hidden = true)
            @AuthUser Long memberId){
        return ApiResponse.onSuccess(myPageService.getMyProfile(memberId));
    }
}
