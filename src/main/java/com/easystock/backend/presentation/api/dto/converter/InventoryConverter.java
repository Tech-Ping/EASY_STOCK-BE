package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesOutputResponse;
import com.easystock.backend.presentation.api.dto.request.TradeRequest;
import com.easystock.backend.presentation.api.dto.response.InventoryResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
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

    public static InventoryResponse toInventoryResponse(Long memberId, Stock stock, Integer quantity, Integer purchasePrice){
        return InventoryResponse.builder()
                .memberId(memberId)
                .stockId(stock.getId())
                .stockName(stock.getName())
                .quantity(quantity)
                .purchasePrice(purchasePrice)
                .build();
    }
}
