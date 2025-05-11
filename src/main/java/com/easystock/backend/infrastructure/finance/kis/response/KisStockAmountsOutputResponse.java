package com.easystock.backend.infrastructure.finance.kis.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KisStockAmountsOutputResponse {
    @JsonProperty("stck_bsop_date")
    private final String stck_bsop_date;

    @JsonProperty("frgn_ntby_tr_pbmn")
    private final String frgn_ntby_tr_pbmn;

    @JsonProperty("prsn_ntby_tr_pbmn")
    private final String prsn_ntby_tr_pbmn;

    @JsonProperty("orgn_ntby_tr_pbmn")
    private final String orgn_ntby_tr_pbmn;
}
