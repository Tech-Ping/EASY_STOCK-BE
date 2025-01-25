package com.easystock.backend.application.service.stock;

import com.easystock.backend.aspect.exception.QuizException;
import com.easystock.backend.aspect.exception.StockException;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.repository.StockRepository;
import com.easystock.backend.infrastructure.kis.converter.KisStockConverter;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesOutputResponse;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesResponse;
import com.easystock.backend.infrastructure.kis.token.KISTokenService;
import com.easystock.backend.presentation.api.dto.converter.StockConverter;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
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

    @Value("${APP_KEY:}")
    private String APP_KEY;

    @Value("${APP_SECRET:}")
    private String APP_SECRET;

    @Value("${KIS_STOCK_PRICE_URL:}")
    private String KIS_STOCK_PRICE_URL;

    @Value("${KIS_STOCK_PRICE_TR_ID:}")
    private String KIS_STOCK_PRICE_TR_ID;

    @Value("${KIS_TRADE_QUOTE_URL:}")
    private String KIS_TRADE_QUOTE_URL;

    @Value("${KIS_TRADE_QUOTE_TR_ID:}")
    private String KIS_TRADE_QUOTE_TR_ID;

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
                        return getStockPriceFromApi(stock);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    } finally {
                        waitNextKsiApi();
                    }
                })
                .filter(stockPricesResponse -> stockPricesResponse != null) // null 필터링
                .collect(Collectors.toList());
    }

    /**
     * 특정 주식의 실시간 시세를 가져오는 메소드
     */
    private StockPricesResponse getStockPriceFromApi(Stock stock) {
        try {
            ResponseEntity<KisStockPricesResponse> kisResponse = kisStockConverter.exchangeRestTemplate(
                    "Bearer " + kisTokenService.getAccessToken(),
                    APP_KEY,
                    APP_SECRET,
                    stock.getCode(),
                    KIS_STOCK_PRICE_URL,
                    KIS_STOCK_PRICE_TR_ID,
                    KisStockPricesResponse.class
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
        }

        return null; // 실패 시 null 반환
    }

    /**
     * 주식 ID를 기반으로 주식의 상세 정보를 가져오는 메소드
     */
    public StockPricesResponse getStockPrice(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        return getStockPriceFromApi(stock); // 주식의 실시간 시세를 가져오는 메소드 호출
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