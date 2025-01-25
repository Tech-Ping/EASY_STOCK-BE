package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class CompleteTutorialResponse {
    @Schema(description = "성공 메시지")
    public String message;
}
