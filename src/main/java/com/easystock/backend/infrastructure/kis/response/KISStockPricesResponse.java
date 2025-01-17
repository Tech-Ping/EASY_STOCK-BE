package com.easystock.backend.infrastructure.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KisStockPricesResponse {

    @JsonProperty("output")
    private final KisStockPricesOutputResponse output;

    @JsonProperty("rt_cd")
    private final String rtCd;

    @JsonProperty("msg_cd")
    private final String msgCd;

    @JsonProperty("msg1")
    private final String msg1;
}