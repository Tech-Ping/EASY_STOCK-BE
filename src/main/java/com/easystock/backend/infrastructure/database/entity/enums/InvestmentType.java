package com.easystock.backend.infrastructure.database.entity.enums;

import lombok.Getter;

@Getter
public enum InvestmentType {
    AGGRESSIVE("공격투자형", "시장 평균 수익률을 넘어서는 높은 수익을 추구하며, 자산가치의 변동에 따른 손실위험을 적극 수용하는 성향을 보였어요.."),
    ACTIVE("적극투자형", "투자원금 보전보다는 위험을 감내하더라도 높은 수준의 투자수익을 추구하시는군요!."),
    NEUTRAL("위험중립형", "다소 높은 위험을 감수할 수 있는 성향이시네요."),
    STABLE_SEEKER("안정추구형", "투자원금의 손실을 최소화하고, 이자소득이나 배당소득 수준의 안정적인 투자를 목표로 하시는 경향을 보였어요."),
    STABLE("안정형", "예금 또는 적금 수준의 수익률을 기대하고, 투자 원금에 손실이 발생하는 것을 원하지 않으시는군요.");

    private final String koreanName;
    private final String description;

    InvestmentType(String koreanName, String description) {
        this.koreanName = koreanName;
        this.description = description;
    }
}
