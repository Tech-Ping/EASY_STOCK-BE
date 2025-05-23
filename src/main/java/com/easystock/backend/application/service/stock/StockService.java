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
import com.easystock.backend.presentation.api.dto.response.*;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    @Value("${KIS_STOCK_FINANCIAL_URL_1:}")
    private String KIS_STOCK_FINANCIAL_URL_1;

    @Value("${KIS_STOCK_FINANCIAL_TR_ID_1:}")
    private String KIS_STOCK_FINANCIAL_TR_ID_1;

    @Value("${KIS_STOCK_FINANCIAL_URL_2:}")
    private String KIS_STOCK_FINANCIAL_URL_2;

    @Value("${KIS_STOCK_FINANCIAL_TR_ID_2:}")
    private String KIS_STOCK_FINANCIAL_TR_ID_2;

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

        RateLimiter rateLimiter = RateLimiter.create(10.0);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<CompletableFuture<StockPricesResponse>> futures = stocks.stream()
                .map(stock -> CompletableFuture.supplyAsync(() -> {
                    rateLimiter.acquire();
                    try {
                        return getStockPriceFromApi(stock);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }, executor))
                .collect(Collectors.toList());

        List<StockPricesResponse> result = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        executor.shutdown();
        return result;
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
    public List<StockAmountResponse> getStockAmountFromApi(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        try {
            ResponseEntity<KisStockAmountsResponse> kisResponse = kisStockConverter.exchangeRestTemplate(
                    "Bearer " + kisTokenService.getAccessToken(),
                    APP_KEY,
                    APP_SECRET,
                    stock.getCode(),
                    KIS_TRADE_AMOUNT_URL,
                    KIS_TRADE_AMOUNT_TR_ID,
                    KisStockAmountsResponse.class
            );

            List<KisStockAmountsOutputResponse> stockOutputs = Optional.ofNullable(kisResponse.getBody())
                    .map(KisStockAmountsResponse::getOutput)
                    .orElse(Collections.emptyList());

            return stockOutputs.stream()
                    .limit(8)
                    .map(StockConverter::toStockAmountReponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Optional<StockInfoResponse> getStockInfo(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

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

            return Optional.ofNullable(kisResponse.getBody())
                    .map(KisStockPricesResponse::getOutput)
                    .map(output -> StockConverter.toStockInfoResponse(stock, output));

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<StockFinancialResponse> getFinancials(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        try {
            ResponseEntity<KisStockMoneysResponse> kisResponse1 = kisStockConverter.exchangeRestTemplate2(
                    "Bearer " + kisTokenService.getAccessToken(),
                    APP_KEY,
                    APP_SECRET,
                    stock.getCode(),
                    KIS_STOCK_FINANCIAL_URL_1,
                    KIS_STOCK_FINANCIAL_TR_ID_1,
                    KisStockMoneysResponse.class
            );

            KisStockMoneysResponse stockApiResponse1 = kisResponse1.getBody();

            ResponseEntity<KisStockFinancialsResponse> kisResponse2 = kisStockConverter.exchangeRestTemplate2(
                    "Bearer " + kisTokenService.getAccessToken(),
                    APP_KEY,
                    APP_SECRET,
                    stock.getCode(),
                    KIS_STOCK_FINANCIAL_URL_2,
                    KIS_STOCK_FINANCIAL_TR_ID_2,
                    KisStockFinancialsResponse.class
            );

            KisStockFinancialsResponse stockApiResponse2 = kisResponse2.getBody();


            if (stockApiResponse1 != null && stockApiResponse2 != null ) {
                List<KisStockMoneysOutputReponse> stockOutputs1 = stockApiResponse1.getOutput();
                List<KisStockFinancialsOutputResponse> stockOutputs2 = stockApiResponse2.getOutput();


                if (stockOutputs1 != null && stockOutputs2 != null) {
                    int limit = Math.min(3, Math.min(stockOutputs1.size(), stockOutputs2.size()));

                    List<StockFinancialResponse> result = new ArrayList<>();
                    for (int i = 0; i < limit; i++) {
                        result.add(StockConverter.toStockFinancialResponse(stockOutputs1.get(i), stockOutputs2.get(i)));
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // 실패 시 null 반환
    }
}
