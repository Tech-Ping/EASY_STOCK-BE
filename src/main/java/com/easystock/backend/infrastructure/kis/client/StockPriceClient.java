package com.easystock.backend.infrastructure.kis.client;

import java.time.LocalDate;

public interface StockPriceClient {
    int getCurrentPrice(String stockCode);
    int getPriceAtDate(String stockCode, LocalDate date);
}
