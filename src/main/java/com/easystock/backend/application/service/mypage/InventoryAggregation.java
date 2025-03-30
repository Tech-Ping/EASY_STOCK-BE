package com.easystock.backend.application.service.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryAggregation {
    int quantity;
    int totalPrice;
    Long stockId;
}
