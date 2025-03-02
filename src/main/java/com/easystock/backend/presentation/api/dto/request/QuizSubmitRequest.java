package com.easystock.backend.presentation.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class QuizSubmitRequest {
    @NotNull(message = "퀴즈 정답 옵션 인덱스")
    private int inputIndex;
}
