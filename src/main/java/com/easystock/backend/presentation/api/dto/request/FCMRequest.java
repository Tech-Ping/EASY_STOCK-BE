package com.easystock.backend.presentation.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FCMRequest {
    @Schema(description = "FCM 토큰", example = "user_device_fcm_token")
    @NotBlank(message = "클라이언트의 FCM 토큰은 공백일 수 없습니다.")
    private String token;

    @Schema(description = "알림 제목", example = "북마크 주식 뉴스 업데이트")
    @NotBlank(message = "FCM 알림 제목은 공백일 수 없습니다.")
    private String title;

    @Schema(description = "알림 내용", example = "오늘의 주식 뉴스 요약을 확인하세요.")
    @NotBlank(message = "FCM 알림 내용은 공백일 수 없습니다.")
    private String body;
}
