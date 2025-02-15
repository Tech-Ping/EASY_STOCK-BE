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
    public static Inventory toInventory(Integer quantity, Integer totalPrice, Member member, Stock stock) {
        return Inventory.builder()
                .quantity(quantity)
                .totalPrice(totalPrice)
                .member(member)
                .stock(stock)
                .build();
    }
}
