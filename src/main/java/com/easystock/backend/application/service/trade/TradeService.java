package com.easystock.backend.application.service.trade;

import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.presentation.api.dto.request.TradeRequest;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResultResponse;

import java.util.List;

public interface TradeService {
    List<TradeResponse> getAllTradesByUser(Long memberId);
    List<TradeResponse> getTradesByStatus(Long memberId, TradeStatus status);
    TradeResultResponse createTrade(Long memberId, TradeRequest request);
    TradeResultResponse cancelTrade(Long memberId, Long tradeId);
}
