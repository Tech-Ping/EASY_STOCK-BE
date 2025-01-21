package com.easystock.backend.infrastructure.kis.converter;

import com.easystock.backend.infrastructure.kis.response.KisStockPricesResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${KIS_STOCK_PRICE_URL}")
    private String KIS_STOCK_PRICE_URL;

    @Value("${KIS_STOCK_PRICE_TR_ID}")
    private String KIS_STOCK_PRICE_TR_ID;

    private final RestTemplate restTemplate;

    /**
     * 한국투자증권 주식현재가 시세 요청 RestTemplate
     * @param accessToken
     * @param appKey
     * @param appSecret
     * @param stockCode
     * @return
     */
    public ResponseEntity<KisStockPricesResponse> exchangeRestTemplate(String accessToken,
                                                                       String appKey,
                                                                       String appSecret,
                                                                       String stockCode) {
        String url = UriComponentsBuilder.fromHttpUrl(KIS_STOCK_PRICE_URL)
                .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                .queryParam("FID_INPUT_ISCD", stockCode)
                .toUriString();

        HttpEntity<Void> entity = getVoidHttpEntity(accessToken, appKey, appSecret);

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                KisStockPricesResponse.class
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
                                               String appSecret) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.set("appkey", appKey);
        headers.set("appsecret", appSecret);
        headers.set("tr_id", KIS_STOCK_PRICE_TR_ID);
        headers.set("Accept", "application/json; charset=utf-8");
        headers.set("Content-Type", "application/json; charset=utf-8");

        return new HttpEntity<>(headers);
    }
}
