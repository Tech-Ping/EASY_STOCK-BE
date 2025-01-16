package com.easystock.backend.infrastructure.database.entity.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("주문 대기"),
    COMPLETED("주문 완료"),
    CANCELED("주문 취소");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }
}
