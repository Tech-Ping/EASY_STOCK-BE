package com.easystock.backend.infrastructure.finance.kis.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KISTokenService {

    private String accessToken;

    /**
     * 주어진 토큰을 그대로 저장합니다.
     * @param accessToken 저장할 accessToken 값
     */
    public void setAccessToken(String accessToken) {
        try {
            this.accessToken = accessToken;  // accessToken 저장
            log.info("token 저장 완료");
        } catch (Exception e) {
            log.error("token 저장 실패", e);
        }
    }

    /**
     * 저장된 토큰을 반환합니다.
     * @return 저장된 accessToken 값
     */
    public String getAccessToken() {
        try {
            if (accessToken != null) {
                return accessToken;  // 저장된 accessToken 반환
            } else {
                log.error("KIS accessToken이 존재하지 않습니다.");
                return null;
            }
        } catch (Exception e) {
            log.error("KIS accessToken 조회 실패", e);
            return null;
        }
    }
}
