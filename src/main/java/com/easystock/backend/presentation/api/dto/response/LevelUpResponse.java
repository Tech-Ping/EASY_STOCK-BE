package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LevelUpResponse {
    @Schema(description = "레벨업된 회원의 레벨")
    private LevelType levelType;

    @Schema(description = "레벨업 성공 메시지")
    private String message;
}
