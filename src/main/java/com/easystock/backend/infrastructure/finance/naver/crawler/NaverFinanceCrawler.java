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

    public int getTodayClosePrice(String stockCode) {
        try {
            String url = NAVER_FINANCE_URL + stockCode;
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .get();

            Elements rows = doc.select("table.type2 tr");

            for (Element row : rows) {
                Elements tds = row.select("td");

                if (tds.size() >= 2) {
                    String dateStr = tds.get(0).text().trim();
                    String priceStr = tds.get(1).text().replace(",", "").trim();

                    if (!dateStr.isEmpty() && !priceStr.isEmpty()) {
                        LocalDate rowDate = LocalDate.parse(dateStr, FORMATTER);

                        if (rowDate.equals(LocalDate.now())) {
                            return Integer.parseInt(priceStr);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
