package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TradeResultResponse {

    private Long tradeId;
    private TradeStatus status;
    private TradeType type;
    private Integer quantity;
    private Integer price;
    private Long customerId;
    private String stockName;
    public String message;
}
