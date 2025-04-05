package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import com.easystock.backend.infrastructure.finance.kis.response.KisStockAmountsOutputResponse;
import com.easystock.backend.infrastructure.finance.kis.response.KisStockPricesOutputResponse;
import com.easystock.backend.infrastructure.finance.kis.response.KisStockQuotesOutput1Response;
import com.easystock.backend.infrastructure.finance.kis.response.KisStockQuotesOutput2Response;
import com.easystock.backend.presentation.api.dto.response.StockAmountResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import com.easystock.backend.presentation.api.dto.response.StockQuotesResponse;
import org.springframework.stereotype.Component;

@Component
public class StockConverter {
    public static StockPricesResponse toStockPricesResponse(
            Stock stock, KisStockPricesOutputResponse stockOutput){
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

    public static StockAmountResponse toStockAmountReponse(
            Stock stock, KisStockAmountsOutputResponse stockOutput){
        return StockAmountResponse.builder()
                .stockName(stock.getName())
                .date(stockOutput.getStck_bsop_date())
                .foreignAmounts(stockOutput.getFrgn_ntby_tr_pbmn())
                .personAmounts(stockOutput.getPrsn_ntby_tr_pbmn())
                .organizationAmounts(stockOutput.getOrgn_ntby_tr_pbmn())
                .build();
    }

    public static StockQuotesResponse toStockQuotesResponse(
            Stock stock, TradeType type, KisStockQuotesOutput1Response stockOutput1, KisStockQuotesOutput2Response stockOutput2) {
        // SELL인 경우 매도 관련 호가와 잔량을 사용하고, BUY인 경우 매수 관련 호가와 잔량을 사용
        if (type == TradeType.SELL) {
            return StockQuotesResponse.builder()
                    .tradeType(type)
                    .stockName(stock.getName())
                    .marketPrice(stockOutput2.getStckPrpr())
                    .ask1(stockOutput1.getAskp1())
                    .ask2(stockOutput1.getAskp2())
                    .ask3(stockOutput1.getAskp3())
                    .ask4(stockOutput1.getAskp4())
                    .ask5(stockOutput1.getAskp5())
                    .ask6(stockOutput1.getAskp6())
                    .ask7(stockOutput1.getAskp7())
                    .ask8(stockOutput1.getAskp8())
                    .ask9(stockOutput1.getAskp9())
                    .ask10(stockOutput1.getAskp10())
                    .quantity1(stockOutput1.getAskpRsqn1())
                    .quantity2(stockOutput1.getAskpRsqn2())
                    .quantity3(stockOutput1.getAskpRsqn3())
                    .quantity4(stockOutput1.getAskpRsqn4())
                    .quantity5(stockOutput1.getAskpRsqn5())
                    .quantity6(stockOutput1.getAskpRsqn6())
                    .quantity7(stockOutput1.getAskpRsqn7())
                    .quantity8(stockOutput1.getAskpRsqn8())
                    .quantity9(stockOutput1.getAskpRsqn9())
                    .quantity10(stockOutput1.getAskpRsqn10())
                    .build();
        } else if (type == TradeType.BUY) {
            return StockQuotesResponse.builder()
                    .tradeType(type)
                    .stockName(stock.getName())
                    .marketPrice(stockOutput2.getStckPrpr())
                    .ask1(stockOutput1.getBidp1())
                    .ask2(stockOutput1.getBidp2())
                    .ask3(stockOutput1.getBidp3())
                    .ask4(stockOutput1.getBidp4())
                    .ask5(stockOutput1.getBidp5())
                    .ask6(stockOutput1.getBidp6())
                    .ask7(stockOutput1.getBidp7())
                    .ask8(stockOutput1.getBidp8())
                    .ask9(stockOutput1.getBidp9())
                    .ask10(stockOutput1.getBidp10())
                    .quantity1(stockOutput1.getBidpRsqn1())
                    .quantity2(stockOutput1.getBidpRsqn2())
                    .quantity3(stockOutput1.getBidpRsqn3())
                    .quantity4(stockOutput1.getBidpRsqn4())
                    .quantity5(stockOutput1.getBidpRsqn5())
                    .quantity6(stockOutput1.getBidpRsqn6())
                    .quantity7(stockOutput1.getBidpRsqn7())
                    .quantity8(stockOutput1.getBidpRsqn8())
                    .quantity9(stockOutput1.getBidpRsqn9())
                    .quantity10(stockOutput1.getBidpRsqn10())
                    .build();
        }

        return null;
    }
}
