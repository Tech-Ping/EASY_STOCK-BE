package com.easystock.backend.presentation.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record AddFcmTokenRequest(
        @NotEmpty
        @Schema(description = "FCM 토큰", example = "fcm-token")
        String fcmToken
){
}
