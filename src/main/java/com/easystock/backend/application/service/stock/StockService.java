package com.easystock.backend.application.service.stock;

import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.repository.StockRepository;
import com.easystock.backend.infrastructure.kis.converter.KisStockConverter;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesOutputResponse;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesResponse;
import com.easystock.backend.infrastructure.kis.token.KISTokenService;
import com.easystock.backend.presentation.api.dto.converter.StockConverter;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StockService {

    @Value("${APP_KEY}")
    private String APP_KEY;

    @Value("${APP_SECRET}")
    private String APP_SECRET;

    private final KisStockConverter kisStockConverter;
    private final KISTokenService kisTokenService;
    private final StockRepository stockRepository;

    /**
     * 한국투자증권 시세 갱신 메소드
     *
     * @return 저장된 주식들의 종목코드, 종목이름, 현재가, 전일 대비, 전일 대비율
     */
    public List<StockPricesResponse> getStockPrices() {
        List<Stock> stocks = stockRepository.findAll();

        return stocks.stream()
                .map(stock -> {
                    try {
                        ResponseEntity<KisStockPricesResponse> kisResponse = kisStockConverter.exchangeRestTemplate(
                                "Bearer " + kisTokenService.getAccessToken(),
                                APP_KEY,
                                APP_SECRET,
                                stock.getCode()
                        );

                        KisStockPricesResponse stockApiResponse = kisResponse.getBody();

                        if (stockApiResponse != null) {
                            KisStockPricesOutputResponse stockOutput = stockApiResponse.getOutput();
                            if (stockOutput != null) {
                                return StockConverter.toStockPricesResponse(stock, stockOutput);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // API 호출 간 대기 시간을 보장
                        waitNextKsiApi();
                    }
                    return null; // null 반환 (예외 발생 또는 처리 불가 시)
                })
                .filter(stockPricesResponse -> stockPricesResponse != null) // null 필터링
                .collect(Collectors.toList());
    }

    /**
     * 한국투자증권 API 요청 대시 메소드
     * 1초에 20회 제한으로 인해 대기 시간 필요
     */
    public static void waitNextKsiApi() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
