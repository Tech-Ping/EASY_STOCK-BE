package com.easystock.backend.presentation.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class CreateMemberRequest {

    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    private String nickname;

    @NotBlank(message = "생년월일은 공백일 수 없습니다.")
    private LocalDate birthDate;

    @NotBlank(message = "ID는 공백일 수 없습니다.")
    private String username;

    @NotBlank(message = "패스워드는 공백일 수 없습니다.")
    private String password;

    @NotBlank(message = "패스워드 확인은 공백일 수 없습니다.")
    private String passwordCheck;
}
