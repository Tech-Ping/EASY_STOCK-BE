package com.easystock.backend.application.service.stock;

import com.easystock.backend.infrastructure.database.repository.StockRecordRepository;
import com.easystock.backend.presentation.api.dto.converter.StockRecordConverter;
import com.easystock.backend.util.Constants;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class StockInfoInitializeService {

    private final StockRecordRepository stockRecordRepository;
    private static final Logger log = LoggerFactory.getLogger(StockInfoInitializeService.class);

    private static final String NAVER_FINANCE_URL = "https://finance.naver.com/item/sise_day.naver?code=";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public boolean fillInitialStockRecords() {
        List<String> stockCodes = Constants.STOCK_CODES;
        LocalDate today = LocalDate.now();
        LocalDate twoMonthAgo = today.minusMonths(2);
        boolean success = true;

        for (String code : stockCodes) {
            try {
                Map<LocalDate, Integer> datePriceMap = new TreeMap<>();
                LocalDate lastValidDate = null;
                int lastValidPrice = 0;

                // step 1) 개장일인 경우 바로 Map에 저장
                for (int i = 1; i <= 10; i++) {
                    String url = NAVER_FINANCE_URL + code + "&page=" + i;

                    Document doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0")
                            .get();

                    Elements rows = doc.select("table.type2 tr");

                    for (Element row : rows) {
                        Elements tds = row.select("td");

                        if (tds.size() >= 2) {
                            String dateStr = tds.get(0).text().trim();
                            String priceStr = tds.get(1).text().replace(",", "").trim();

                            if (!dateStr.isEmpty() && !priceStr.isEmpty() && !priceStr.equals("-")) {
                                LocalDate rowDate = LocalDate.parse(dateStr, FORMATTER);
                                int closePrice = Integer.parseInt(priceStr);
                                datePriceMap.put(rowDate, closePrice);
                            }
                        }
                    }
                }

                // step 2) 휴장일인 경우 유효한 날짜
                for (LocalDate date = twoMonthAgo; !date.isAfter(today); date = date.plusDays(1)) {
                    if (datePriceMap.containsKey(date)) {
                        lastValidPrice = datePriceMap.get(date);
                        lastValidDate = date;
                    }

                    if (lastValidDate == null) continue;

                    boolean exists = stockRecordRepository.existsByStockCodeAndDate(code, date);
                    if (!exists) {
                        stockRecordRepository.save(
                                StockRecordConverter.toRecord(code, date, lastValidPrice)
                        );
                        log.info("보간 저장: {} / {} - {}원", code, date, lastValidPrice);
                    }
                }
            } catch (Exception e) {
                log.error("초기화 실패: {} - {}", code, e.getMessage(), e);
                success = false;
            }
        }
        return success;
    }


}
