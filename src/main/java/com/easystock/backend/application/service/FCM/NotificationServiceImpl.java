package com.easystock.backend.application.service.FCM;

import com.easystock.backend.presentation.config.FirebaseConfig;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@ConditionalOnBean(FirebaseConfig.class)
public class NotificationServiceImpl implements NotificationService {
    private final FirebaseMessaging firebaseMessaging;
    @Override
    public void sendMessage(Message message){
        try {
            String response = String.valueOf(firebaseMessaging.sendAsync(message).get());
            log.info("✅ FCM 메시지 전송 성공! 응답 메시지 ID: {}", response);
        } catch (Exception e) {
            log.error("❌ FCM 메시지 전송 실패: ", e);
        }
    }
}
