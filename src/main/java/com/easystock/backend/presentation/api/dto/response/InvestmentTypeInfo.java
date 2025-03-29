package com.easystock.backend.presentation.api.dto.response;

import com.easystock.backend.infrastructure.database.entity.enums.InvestmentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvestmentTypeInfo {
    private String type;
    private String description;

    public static InvestmentTypeInfo from(InvestmentType investmentType) {
        return InvestmentTypeInfo.builder()
                .type(investmentType.getKoreanName())
                .description(investmentType.getDescription())
                .build();
    }
}
