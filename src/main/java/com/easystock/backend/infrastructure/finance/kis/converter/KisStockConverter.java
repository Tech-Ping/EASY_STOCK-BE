package com.easystock.backend.infrastructure.finance.kis.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Component
public class KisStockConverter {

    private final RestTemplate restTemplate;

    /**
     * 한국투자증권 주식 현재가 시세 요청 RestTemplate
     * 이 메소드는 주식 시세를 요청하는데 사용되며, URL 및 응답 타입을 동적으로 설정할 수 있습니다.
     * @param accessToken 인증에 필요한 액세스 토큰
     * @param appKey 애플리케이션 키
     * @param appSecret 애플리케이션 비밀 키
     * @param stockCode 주식 코드
     * @param stockUrl 요청을 보낼 기본 URL (예: 주식 시세 URL)
     * @param stockTrId 요청을 보낼 TrId
     * @param responseType 응답 데이터 타입 (예: KisStockPricesResponse.class)
     * @param <T> 응답 타입
     * @return 주식 시세 정보가 포함된 ResponseEntity
     */
    public <T> ResponseEntity<T> exchangeRestTemplate(String accessToken,
                                                      String appKey,
                                                      String appSecret,
                                                      String stockCode,
                                                      String stockUrl,
                                                      String stockTrId,
                                                      Class<T> responseType) {
        String url = UriComponentsBuilder.fromHttpUrl(stockUrl)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", stockCode)
                .toUriString();

        HttpEntity<Void> entity = getVoidHttpEntity(accessToken, appKey, appSecret, stockTrId);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType
        );
    }

    public <T> ResponseEntity<T> exchangeRestTemplate2(String accessToken,
                                                      String appKey,
                                                      String appSecret,
                                                      String stockCode,
                                                      String stockUrl,
                                                      String stockTrId,
                                                      Class<T> responseType) {
        String url = UriComponentsBuilder.fromHttpUrl(stockUrl)
                .queryParam("FID_DIV_CLS_CODE", "1")
                .queryParam("fid_cond_mrkt_div_code", "J")
                .queryParam("fid_input_iscd", stockCode)
                .toUriString();

        HttpEntity<Void> entity = getVoidHttpEntity(accessToken, appKey, appSecret, stockTrId);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                responseType
        );
    }

    /**
     * 한국투자증권 주식현재가 시세 요청 헤더
     * @param accessToken
     * @param appKey
     * @param appSecret
     * @return
     */
    private HttpEntity<Void> getVoidHttpEntity(String accessToken,
                                               String appKey,
                                               String appSecret,
                                               String stockTrId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", stockTrId);
        headers.set("tr_cont", "N");
        headers.set("Accept", "application/json; charset=utf-8");
        headers.set("Content-Type", "application/json; charset=utf-8");

        return new HttpEntity<>(headers);
    }
}
