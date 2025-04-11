package com.easystock.backend.infrastructure.finance.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class KisStockMoneysResponse {
    @JsonProperty("output")
    private final List<KisStockMoneysOutputReponse> output;

    @JsonProperty("rt_cd")
    private final String rtCd;

    @JsonProperty("msg_cd")
    private final String msgCd;

    @JsonProperty("msg1")
    private final String msg1;
}
