package com.easystock.backend.infrastructure.kis.token;

import com.easystock.backend.infrastructure.kis.response.KisTokenResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KISTokenScheduler {

    private final RestTemplate restTemplate;
    private final KISTokenService tokenService;

    @Value("${KIS_TOKEN_URL}")
    private String KIS_TOKEN_URL;

    @Value("${APP_KEY}")
    private String APP_KEY;

    @Value("${APP_SECRET}")
    private String APP_SECRET;

    /**
     * 애플리케이션 시작 시 한국투자증권 accssToken 초기화 작업
     * @throws JsonProcessingException
     */
    @PostConstruct
    public void init() throws JsonProcessingException {
        log.info("애플리케이션 시작 시 토큰 갱신 시작");
        refreshToken(APP_KEY, APP_SECRET);
    }

    /**
     * 오전 08시 30분 한국투자증권 API key 갱신 스케줄러
     * @throws JsonProcessingException
     */
    @Scheduled(cron = "0 30 8 * * ?")
    public void getToken() throws JsonProcessingException {
        // 갱신할 토큰을 각각 요청
        refreshToken(APP_KEY, APP_SECRET);
    }

    /**
     * 주어진 APP_KEY와 APP_SECRET로 토큰을 갱신합니다.
     * @param appKey    API Key
     * @param appSecret API Secret
     * @throws JsonProcessingException
     */
    private void refreshToken(String appKey, String appSecret) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        body.put("appkey", appKey);
        body.put("appsecret", appSecret);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(body);

        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {
            ResponseEntity<KisTokenResponse> response = restTemplate.exchange(
                    KIS_TOKEN_URL, HttpMethod.POST, request, KisTokenResponse.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                KisTokenResponse tokenResponse = response.getBody();
                if (tokenResponse != null) {
                    String accessToken = tokenResponse.getAccessToken();
                    tokenService.setAccessToken(accessToken);
                    log.info("08:30 스케줄러 : AccessToken 갱신 성공");
                } else {
                    log.info("response가 null입니다.");
                }
            } else {
                log.info("AccessToken 갱신 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("AccessToken API 요청 오류");
        }
    }
}