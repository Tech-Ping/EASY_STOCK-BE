package com.easystock.backend.infrastructure.finance.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KisStockQuotesOutput2Response {

    @JsonProperty("stck_prpr")
    private final String stckPrpr; // 주식 현재가
}
