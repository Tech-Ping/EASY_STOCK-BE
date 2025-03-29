package com.easystock.backend.infrastructure.kis.client;

import com.easystock.backend.infrastructure.kis.client.StockPriceClient;
import com.easystock.backend.infrastructure.kis.converter.KisStockConverter;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesOutputResponse;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesResponse;
import com.easystock.backend.infrastructure.kis.token.KISTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class KisStockPriceClient implements StockPriceClient {
    private final KISTokenService kisTokenService;
    private final KisStockConverter kisStockConverter;

    @Value("${APP_KEY}") private String APP_KEY;
    @Value("${APP_SECRET}") private String APP_SECRET;
    @Value("${KIS_STOCK_PRICE_URL}") private String KIS_STOCK_PRICE_URL;
    @Value("${KIS_STOCK_PRICE_TR_ID}") private String KIS_STOCK_PRICE_TR_ID;

    @Override
    public int getCurrentPrice(String stockCode) {
        try {
            ResponseEntity<KisStockPricesResponse> response = kisStockConverter.exchangeRestTemplate(
                    "Bearer " + kisTokenService.getAccessToken(),
                    APP_KEY,
                    APP_SECRET,
                    stockCode,
                    KIS_STOCK_PRICE_URL,
                    KIS_STOCK_PRICE_TR_ID,
                    KisStockPricesResponse.class
            );

            KisStockPricesOutputResponse output = response.getBody().getOutput();
            return Integer.parseInt(output.getStckPrpr());
        } catch (Exception e) {
            throw new RuntimeException("KIS 현재가 조회 실패", e);
        }
    }

    @Override
    public int getPriceAtDate(String stockCode, LocalDate date) {
        // TODO: KIS 에서 날짜 중심으로 조회할 수 있는 API가 있는지 확인 후 구현
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
