package com.easystock.backend.application.service.stock;

import com.easystock.backend.application.service.trade.TradeService;
import com.easystock.backend.aspect.exception.StockException;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import com.easystock.backend.infrastructure.database.repository.StockRepository;
import com.easystock.backend.infrastructure.finance.kis.converter.KisStockConverter;
import com.easystock.backend.infrastructure.finance.kis.response.*;
import com.easystock.backend.infrastructure.finance.kis.token.KISTokenService;
import com.easystock.backend.presentation.api.dto.converter.StockConverter;
import com.easystock.backend.presentation.api.dto.response.StockAmountResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import com.easystock.backend.presentation.api.dto.response.StockQuotesResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    @Value("${KIS_TRADE_AMOUNT_URL:}")
    private String KIS_TRADE_AMOUNT_URL;

    @Value("${KIS_TRADE_AMOUNT_TR_ID:}")
    private String KIS_TRADE_AMOUNT_TR_ID;

    private final KisStockConverter kisStockConverter;
    private final KISTokenService kisTokenService;
    private final StockRepository stockRepository;
    private final TradeService tradeService;

    /**
     * 한국투자증권 시세 갱신 메소드
     *
     * @return 저장된 주식들의 종목코드, 종목이름, 현재가, 전일 대비, 전일 대비율
     */
    @Transactional
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
    public StockPricesResponse getStockPriceFromApi(Stock stock) {
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
                    StockPricesResponse stockPrice = StockConverter.toStockPricesResponse(stock, stockOutput);

                    // 거래 상태 업데이트
                    tradeService.checkTradeStatus(stock, stockPrice.getStckPrpr());
                    log.info("Stock: {}, Current Price: {}", stock.getName(), stockPrice.getStckPrpr());

                    return stockPrice;
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
    @Transactional
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

    /**
     * 특정 주식의 실시간 호가와 잔량을 가져오는 메소드
     */
    private StockQuotesResponse getStockQuotesFromApi(Stock stock, TradeType type) {
        try {
            ResponseEntity<KisStockQuotesResponse> kisResponse = kisStockConverter.exchangeRestTemplate(
                    "Bearer " + kisTokenService.getAccessToken(),
                    APP_KEY,
                    APP_SECRET,
                    stock.getCode(),
                    KIS_TRADE_QUOTE_URL,
                    KIS_TRADE_QUOTE_TR_ID,
                    KisStockQuotesResponse.class
            );

            KisStockQuotesResponse stockApiResponse = kisResponse.getBody();

            if (stockApiResponse != null) {

                KisStockQuotesOutput1Response stockOutput1 = stockApiResponse.getOutput1();
                KisStockQuotesOutput2Response stockOutput2 = stockApiResponse.getOutput2();

                if (stockOutput1 != null && stockOutput2 != null) {
                    return StockConverter.toStockQuotesResponse(stock, type, stockOutput1, stockOutput2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 주식 ID, 매도/매수를 기반으로 특정 주식의 실시간 호가와 잔량을 가져오는 메소드
     */
    public StockQuotesResponse getStockQuotes(Long stockId, TradeType type) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        return getStockQuotesFromApi(stock, type);
    }


    /**
     * 특정 주식의 8일간의 투자자별 순매수 거래 대금 정보를 가져오는 메소드
     */
    public List<StockAmountResponse> getStockAmounts(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        List<StockAmountResponse> result = new ArrayList<>();
        LocalDate date = LocalDate.now().minusDays(1); // 어제부터 시작
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        while (result.size() < 8) {
            String formattedDate = date.format(formatter);
            StockAmountResponse response = getStockAmountFromApi(stock, formattedDate);

            if (response != null) {
                result.add(response);
            }

            date = date.minusDays(1); // 하루 전으로 이동
        }

        return result;
    }

    private StockAmountResponse getStockAmountFromApi(Stock stock, String date) {
        try {
            ResponseEntity<KisStockAmountsResponse> kisResponse = kisStockConverter.exchangeRestTemplate2(
                    "Bearer " + kisTokenService.getAccessToken(),
                    APP_KEY,
                    APP_SECRET,
                    stock.getCode(),
                    KIS_TRADE_AMOUNT_URL,
                    KIS_TRADE_AMOUNT_TR_ID,
                    date,
                    KisStockAmountsResponse.class
            );

            KisStockAmountsResponse stockApiResponse = kisResponse.getBody();

            if (stockApiResponse != null) {
                KisStockAmountsOutputResponse stockOutput = stockApiResponse.getOutput();
                if (stockOutput != null) {
                    return StockConverter.toStockAmountReponse(stock, stockOutput);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
