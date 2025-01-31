package com.easystock.backend.presentation.api.dto.request;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class TradeRequest {

    private TradeType type;
    private Integer quantity;
    private Integer tradePrice;
    private Integer currentPrice;
    private Long memberId;
    private Long stockId;
}
