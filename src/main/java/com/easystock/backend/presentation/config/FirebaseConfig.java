package com.easystock.backend.presentation.config;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Configuration
public class FirebaseConfig {
    @Value("${cloud.firebase}")
    private String firebaseSecret;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        log.info("Firebase Messaging initalize 시작!");
        ByteArrayInputStream credentials = new ByteArrayInputStream(Base64.getDecoder().decode(firebaseSecret));
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(credentials)).build();
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}
