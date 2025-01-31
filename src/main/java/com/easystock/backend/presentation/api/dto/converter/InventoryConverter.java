package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.presentation.api.dto.request.TradeRequest;
import org.springframework.stereotype.Component;

@Component
public class InventoryConverter {
    public static Inventory toInventory(Trade trade, Member member, Stock stock) {
        return Inventory.builder()
                .quantity(trade.getQuantity())
                .price(trade.getPrice())
                .member(member)
                .stock(stock)
                .build();
    }
}
