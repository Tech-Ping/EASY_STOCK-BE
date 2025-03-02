package com.easystock.backend.presentation.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public class LoginMemberRequest {
    @NotBlank(message = "유저 ID는 공백일 수 없습니다.")
    public String username;

    @NotBlank(message = "유저 비밀번호는 공백일 수 없습니다.")
    public String password;
}
