package com.easystock.backend.application.service.FCM;

import com.google.firebase.messaging.Message;

public interface NotificationService {
    void sendMessage(Message message);
}
