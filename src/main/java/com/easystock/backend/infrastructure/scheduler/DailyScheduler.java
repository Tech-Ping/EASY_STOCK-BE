package com.easystock.backend.infrastructure.scheduler;

import com.easystock.backend.infrastructure.database.repository.StockRecordRepository;
import com.easystock.backend.infrastructure.finance.naver.crawler.NaverFinanceCrawler;
import com.easystock.backend.presentation.api.dto.converter.StockRecordConverter;
import com.easystock.backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyScheduler {
    private final StockRecordRepository stockRecordRepository;
    private final NaverFinanceCrawler naverFinanceCrawler;
    private static final Logger log = LoggerFactory.getLogger(DailyScheduler.class);

    @Scheduled(cron = "0 40 2 * * *")
    public void saveDailyStockPrices() {
        List<String> stockCodes = Constants.STOCK_CODES;
        LocalDate today = LocalDate.now();

        for(String code: stockCodes) {
            try {
                int closePrice = naverFinanceCrawler.getClosePrice(code);
                boolean exists = stockRecordRepository.existsByStockCodeAndDate(code, today);
                if(!exists) {
                    stockRecordRepository.save(StockRecordConverter.toRecord(code, today, closePrice));
                    log.info("종가 레코드 저장 완료: {} - {}원", code, closePrice);
                } else {
                    log.info("이미 저장된 일별 종가 레코드 : {}", code);
                }
            } catch(Exception e) {
                log.error("{} 저장 실패: {}", code, e.getMessage());
            }
        }
    }

}
