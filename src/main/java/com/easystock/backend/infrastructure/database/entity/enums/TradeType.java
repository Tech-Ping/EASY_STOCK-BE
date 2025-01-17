package com.easystock.backend.infrastructure.database.entity.enums;

import lombok.Getter;

@Getter
public enum TradeType {
    BUY("매수"),
    SELL("매도");

    private final String value;

    TradeType(String value) {
        this.value = value;
    }
}
