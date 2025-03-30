package com.easystock.backend.application.service.mypage;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.StockRecord;
import com.easystock.backend.infrastructure.database.entity.enums.InvestmentType;
import com.easystock.backend.infrastructure.database.repository.InventoryRepository;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.infrastructure.database.repository.StockRecordRepository;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import com.easystock.backend.util.DateUtils;
import com.easystock.backend.util.FormatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InvestmentTypeAnalyzer {

    private final MemberRepository memberRepository;
    private final InventoryRepository inventoryRepository;
    private final StockRecordRepository stockRecordRepository;

    /**
     * 투자자 성향을 분석합니다: 전날 종가 vs 한달 전 종가 기준으로 총 수익률 분석
     */
    public InvestmentType analyze(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Inventory> inventories = inventoryRepository.findAllByMember(member);

        int totalCurrentValue = 0;
        int totalPastValue = 0;

        for (Inventory inventory : inventories) {
            String stockCode = inventory.getStock().getCode();
            int quantity = inventory.getQuantity();

            // 전날 종가 기준 현재 평가금액
            int yesterdayPrice = stockRecordRepository.findTopByStockCodeAndDateBeforeOrderByDateDesc(stockCode, LocalDate.now())
                    .map(StockRecord::getClosePrice)
                    .orElse(0);

            // 한달 전 종가 기준 평가금액
            int oneMonthAgoPrice = stockRecordRepository
                    .findTopByStockCodeAndDateBeforeOrderByDateDesc(stockCode, DateUtils.getValidOneMonthAgo())
                    .map(StockRecord::getClosePrice)
                    .orElse(0);

            totalCurrentValue += yesterdayPrice * quantity;
            totalPastValue += oneMonthAgoPrice * quantity;
        }

        if (totalPastValue == 0) return InvestmentType.STABLE;
        Double profitRate = FormatUtils.calculateChangeRate(totalCurrentValue, totalPastValue);

        if (profitRate == null) return null;
        if (profitRate >= 15.0) return InvestmentType.AGGRESSIVE;
        if (profitRate >= 7.0) return InvestmentType.ACTIVE;
        if (profitRate >= 3.0) return InvestmentType.NEUTRAL;
        if (profitRate >= 0.0) return InvestmentType.STABLE_SEEKER;

        return InvestmentType.STABLE;
    }
}
