package com.easystock.backend.util;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

import java.time.LocalDateTime;

public class FcmAlarmUtils {
    public static WebpushConfig buildWebpushConfig(String title, String body, LocalDateTime sendTimeStamp){
        return WebpushConfig.builder()
                .setNotification(
                        WebpushNotification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .putData("sendTimeStamp", sendTimeStamp.toString())
                .build();
    }

    public static Message buildWebpushMessage(String token, String title, String body, LocalDateTime sendTimeStamp){
        WebpushConfig webpushConfig = buildWebpushConfig(title, body, sendTimeStamp);
        return Message.builder()
                .setToken(token)
                .setWebpushConfig(webpushConfig)
                .build();
    }
}
