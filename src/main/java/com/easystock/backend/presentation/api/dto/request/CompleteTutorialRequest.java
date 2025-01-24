package com.easystock.backend.presentation.api.dto.request;


import jakarta.validation.constraints.NotNull;

public class CompleteTutorialRequest {
    @NotNull(message = "튜토리얼 ID는 공백일 수 없습니다.")
    private String tutorialId;
}
