package com.easystock.backend.infrastructure.database.entity.enums;

import lombok.Getter;

@Getter
public enum OrderType {
    BUY("매수"),
    SELL("매도");

    private final String value;

    OrderType(String value) {
        this.value = value;
    }
}
