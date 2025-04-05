package com.easystock.backend.infrastructure.finance.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KisStockMoneysOutputReponse {
    @JsonProperty("stac_yymm")
    private final String stac_yymm;

    @JsonProperty("total_aset")
    private final String total_aset;

    @JsonProperty("total_lblt")
    private final String total_lblt;

    @JsonProperty("total_cptl")
    private final String total_cptl;

    @JsonProperty("cpfn")
    private final String cpfn;
}
