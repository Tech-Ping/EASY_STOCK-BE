package com.easystock.backend.infrastructure.finance.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KisStockFinancialsOutputResponse {
    @JsonProperty("stac_yymm")
    private final String stac_yymm;

    @JsonProperty("sale_account")
    private final String sale_account;

    @JsonProperty("bsop_prti")
    private final String bsop_prti;

    @JsonProperty("thtr_ntin")
    private final String thtr_ntin;
}
