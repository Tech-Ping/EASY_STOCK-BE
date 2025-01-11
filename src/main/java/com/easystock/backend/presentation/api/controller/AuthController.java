package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.auth.AuthService;
import com.easystock.backend.aspect.payload.ApiResponse;
import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.request.LoginMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;
import com.easystock.backend.presentation.api.dto.response.LoginMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Tag(name = "유저 인증/인가 API - /api/members ")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/join")
    @Operation(summary = "유저 회원가입 API - 유저가 회원가입을 요청합니다.",
            description = """
                     \s
                     [RequestBody] \s
                     * nickname: String (유저 닉네임) \s
                     * birthDate: "yyyy-mm-dd" \s
                     * username: String (유저 ID) \s
                     * password: String (유저 비밀번호) \s
                     * passwordCheck: String (유저 비밀번호 확인) \s
                     * isAgreed : Boolean (약관 동의 여부 | true여야만 가입이 가능) \s
                    \s""")
    public ApiResponse<CreateMemberResponse> joinMember(
            @RequestBody @Valid CreateMemberRequest request) {
        return ApiResponse.onSuccess(authService.createMember(request));
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인 API - 유저가 로그인을 진행합니다.",
                description = """
                     \s
                     [RequestBody] \s
                     * username : Stirng (유저 ID) \s
                     * password : String (유저 비밀번호) \s
                    \s"
                """)
    public ApiResponse<LoginMemberResponse> loginMember(
            @RequestBody @Valid LoginMemberRequest request) {
        return ApiResponse.onSuccess(authService.loginMember(request));
    }
}
