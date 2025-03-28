package com.easystock.backend.application.service.inventory;

import com.easystock.backend.application.service.level.LevelService;
import com.easystock.backend.aspect.exception.StockException;
import com.easystock.backend.aspect.exception.TradeException;
import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.repository.InventoryRepository;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.converter.InventoryConverter;
import com.easystock.backend.presentation.api.dto.response.InventoryResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public List<InventoryResponse> getInventories(Long memberId) {

        List<Inventory> inventories = inventoryRepository.findByMemberId(memberId)
                .orElseThrow(() -> new StockException(ErrorStatus.NO_STOCK_OWNED));

        return inventories.stream()
                .map(inventory -> InventoryConverter.toInventoryResponse(
                        memberId,
                        inventory.getStock(),
                        inventory.getQuantity(),
                        inventory.getTotalPrice() / inventory.getQuantity() // 매입가 계산 (총 가격 / 수량)
                ))
                .collect(Collectors.toList());
    }
}
