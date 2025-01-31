package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.request.TradeRequest;
import com.easystock.backend.presentation.api.dto.response.LevelUpResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResultResponse;
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

    public static Trade toTrade(TradeRequest request, Member member, Stock stock, TradeStatus status) {
        return Trade.builder()
                .status(status)
                .type(request.getType())
                .quantity(request.getQuantity())
                .price(request.getTradePrice())
                .customer(member)
                .stock(stock)
                .build();
    }

    public static TradeResultResponse toTradeResultResponse(Trade trade, Member member, Stock stock){
        return TradeResultResponse.builder()
                .tradeId(trade.getId())
                .status(trade.getStatus())
                .type(trade.getType())
                .quantity(trade.getQuantity())
                .price(trade.getPrice())
                .customerId(member.getId())
                .stockName(stock.getName())
                .message("주문이 정상적으로 접수되었습니다.")
                .build();
    }
}
