package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.auth.AuthService;
import com.easystock.backend.aspect.payload.ApiResponse;
import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "유저 인증/인가 API - /api/members ")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/join")
    @Operation(summary = "유저 회원가입 API - 유저가 회원가입을 요청합니다.",
            description = """
                     개발 편의를 위한 테스트 계정 토큰 발급 api - **Swagger에서만 사용**
                                        \s
                     memberId 값에 따라 토큰 발급 (1 ~ 5)
                    \s""")
    public ApiResponse<CreateMemberResponse> joinMember(
            @RequestBody @Valid CreateMemberRequest request) {
        return ApiResponse.onSuccess(authService.createMember(request));
    }
}
