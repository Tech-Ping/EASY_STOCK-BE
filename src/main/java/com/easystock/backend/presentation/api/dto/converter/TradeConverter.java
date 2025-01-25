package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import org.springframework.stereotype.Component;

@Component
public class TradeConverter {
    public static TradeResponse toTradeResponse(Trade trade) {
        return TradeResponse.builder()
                .tradeId(trade.getId()) // 거래 ID
                .status(trade.getStatus()) // 거래 상태
                .type(trade.getType()) // 거래 유형
                .quantity(trade.getQuantity()) // 거래 수량
                .price(trade.getPrice()) // 거래 가격
                .customerId(trade.getCustomer().getId()) // 고객 ID
                .stockId(trade.getStock().getId()) // 주식 ID
                .stockName(trade.getStock().getName()) // 주식 이름
                .build();
    }
}
