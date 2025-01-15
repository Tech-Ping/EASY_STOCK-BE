package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetMemberProfileResponse {
    @Schema(description = "회원의 프로필 이미지")
    private int profileImage;

    @Schema(description = "회원의 레벨")
    private LevelType level;

    @Schema(description = "회원의 보유 STOKEN")
    private int tokenBudget;

    @Schema(description = "회원의 닉네임")
    private String nickname;

    @Schema(description = "회원의 경험치 XP")
    private int xpGuage;
}
