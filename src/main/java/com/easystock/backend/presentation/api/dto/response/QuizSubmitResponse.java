package com.easystock.backend.presentation.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class QuizSubmitResponse {
    @Schema(description = "정답 여부")
    private boolean isCorrect;

    @Schema(description = "사용자가 제출한 퀴즈 답변에 대한 반환값을 제공합니다.")
    private String message;

    @Schema(description = "추가된 경험치")
    private int addedXp;

    @Schema(description = "총 경험치")
    private int totalXp;
}
