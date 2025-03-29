package com.easystock.backend.application.service.inventory;

import com.easystock.backend.presentation.api.dto.response.InventoryResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;

import java.util.List;

public interface InventoryService {
    List<InventoryResponse> getInventories(Long memberId);
}
