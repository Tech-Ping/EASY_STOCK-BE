package com.easystock.backend.infrastructure.finance.naver.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class NaverFinanceCrawler {

    private static final String NAVER_FINANCE_URL = "https://finance.naver.com/item/sise_day.naver?code=";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static int getClosePrice(String stockCode) {
        try {
            Document doc = Jsoup.connect(NAVER_FINANCE_URL + stockCode)
                    .userAgent("Mozilla/5.0")
                    .get();

            Elements rows = doc.select("table.type2 tr");
            LocalDate oneMonthAgoData = LocalDate.now().minusMonths(1);

            for(Element row: rows) {
                Elements tds = row.select("td");

                if(tds.size() >= 2) {
                    String dateStr = tds.get(0).text();
                    String priceStr = tds.get(1).text().replace(",", "");

                    if (!dateStr.isEmpty() && !priceStr.isEmpty()) {
                        LocalDate rowDate = LocalDate.parse(dateStr, FORMATTER);

                        if (!rowDate.isAfter(oneMonthAgoData)) {
                            return Integer.parseInt(priceStr);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
