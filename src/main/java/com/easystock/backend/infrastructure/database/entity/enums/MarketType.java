package com.easystock.backend.infrastructure.database.entity.enums;

import lombok.Getter;

@Getter
public enum MarketType {
    KOSPI("코스피"),
    KOSDAQ("코스닥");

    private final String value;

    MarketType(String value) {
        this.value = value;
    }
}