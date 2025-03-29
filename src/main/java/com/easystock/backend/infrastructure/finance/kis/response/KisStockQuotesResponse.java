package com.easystock.backend.infrastructure.finance.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KisStockQuotesResponse {
    @JsonProperty("output1")
    private final KisStockQuotesOutput1Response output1;

    @JsonProperty("output2")
    private final KisStockQuotesOutput2Response output2;

    @JsonProperty("rt_cd")
    private final String rtCd;

    @JsonProperty("msg_cd")
    private final String msgCd;

    @JsonProperty("msg1")
    private final String msg1;
}
