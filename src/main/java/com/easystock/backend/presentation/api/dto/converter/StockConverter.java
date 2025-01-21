package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.kis.response.KisStockPricesOutputResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import org.springframework.stereotype.Component;

@Component
public class StockConverter {
    public static StockPricesResponse toStockPricesResponse(Stock stock, KisStockPricesOutputResponse stockOutput){
        return StockPricesResponse.builder()
                .id(stock.getId())
                .stockCode(stock.getCode())
                .stockName(stock.getName())
                .stockInfo(stock.getInfo())
                .imgUrl(stock.getImgUrl())
                .stockIndustry(stock.getType())
                .stckPrpr(Long.parseLong(stockOutput.getStckPrpr()))
                .prdyVrss(Long.parseLong(stockOutput.getPrdyVrss()))
                .prdyCtrt(Double.parseDouble(stockOutput.getPrdyCtrt()))
                .acmlTrPbmn(Long.parseLong(stockOutput.getAcmlTrPbmn()))
                .acmlVol(Long.parseLong(stockOutput.getAcmlVol()))
                .build();
    }
}
