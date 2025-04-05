package com.easystock.backend.infrastructure.finance.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KisStockAmountsResponse {
     @JsonProperty("output")
     private final KisStockAmountsOutputResponse output;

     @JsonProperty("rt_cd")
     private final String rtCd;

     @JsonProperty("msg_cd")
     private final String msgCd;

     @JsonProperty("msg1")
     private final String msg1;
}

