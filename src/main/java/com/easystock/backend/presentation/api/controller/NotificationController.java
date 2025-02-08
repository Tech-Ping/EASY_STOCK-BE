package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.FCM.NotificationService;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.util.FCMAlarmUtil;
import com.google.firebase.messaging.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "알림 API - /api/notifications ")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/test")
    @Operation(
            summary = "푸시 알림 전송 테스트용 API - 회원이 알림 전송을 테스트합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<String> sendTest(
            @RequestParam String fcmToken
    ){
        Message message = FCMAlarmUtil.buildWebpushMessage(
                 fcmToken,
                 "[💸이지스톡💸] 푸시 알림 테스트 💫",
                "잘 전송이 되나 확인되는 FCM 테스트 메시지입니다! 😊😊",
                LocalDateTime.now()
        );

        notificationService.sendMessage(message);
        return ApiResponse.onSuccess("푸시 알림 전송 테스트 전송에 성공하였습니다.");
    }
}
