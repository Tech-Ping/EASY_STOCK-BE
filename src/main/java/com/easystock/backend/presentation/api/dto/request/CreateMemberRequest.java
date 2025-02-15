package com.easystock.backend.presentation.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class CreateMemberRequest {

    @NotBlank(message = "닉네임은 공백일 수 없습니다.")
    public String nickname;

    @NotNull(message = "생년월일은 공백일 수 없습니다.")
    @Past @DateTimeFormat(pattern = "yyyy-MM-dd")
    public LocalDate birthDate;

    @NotBlank(message = "ID는 공백일 수 없습니다.")
    public String username;

    @NotBlank(message = "패스워드는 공백일 수 없습니다.")
    public String password;

    @NotBlank(message = "패스워드 확인은 공백일 수 없습니다.")
    public String passwordCheck;

    @NotNull
    public Boolean isAgreed;
}
