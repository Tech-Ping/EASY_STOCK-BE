package com.easystock.backend.infrastructure.finance.naver.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NaverFinanceCrawler {

    static final String NAVER_FINANCE_URL = "https://finance.naver.com/item/sise_day.naver?code=";

    public static int getLastMonthPrice(String stockCode) {
        try {
            Document doc = Jsoup.connect(NAVER_FINANCE_URL)
                    .userAgent("Mozzila/5.0")
                    .get();

            Elements rows = doc.select("table.type2 tr");

            for(Element row: rows) {
                Elements tds = row.select("td");

                if(tds.size() >= 2) {
                    String date = tds.get(0).text();
                    String priceStr = tds.get(1).text().replace(",", "");

                    if(!priceStr.isEmpty()) {
                        int price = Integer.parseInt(priceStr);
                        return price;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
