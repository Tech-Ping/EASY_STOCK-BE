package com.easystock.backend.application.service.mypage;

import com.easystock.backend.application.service.stock.StockService;
import com.easystock.backend.aspect.exception.QuizException;
import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.StockRecord;
import com.easystock.backend.infrastructure.database.repository.InventoryRepository;
import com.easystock.backend.infrastructure.database.repository.StockRecordRepository;
import com.easystock.backend.presentation.api.dto.converter.MemberConverter;
import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.converter.StockRecordConverter;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;
import com.easystock.backend.presentation.api.dto.response.StockPricesResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service @AllArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final MemberRepository memberRepository;
    private final InventoryRepository inventoryRepository;
    private final StockRecordRepository stockRecordRepository;
    private final StockService stockService;

    @Override
    public GetMemberProfileResponse getMyProfile(Long memberId){
        return memberRepository.findById(memberId)
                .map(MemberConverter::toProfileResDto)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public List<MonthlyStockInfoResponse> getMyCurrentStockStatus(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new QuizException(ErrorStatus.MEMBER_NOT_FOUND));

        List<Inventory> myStocks = inventoryRepository.findAllByMember(member);

        return myStocks.stream()
                .map(inventory -> {
                    Long stockId = inventory.getStock().getId();
                    String stockCode = inventory.getStock().getCode();

                    StockPricesResponse currentPriceInfo = stockService.getStockPrice(stockId);
                    int currentPrice = Math.toIntExact(currentPriceInfo.getStckPrpr());
                    String stockName = currentPriceInfo.getStockName();

                    int lastMonthPrice = stockRecordRepository
                            .findRecentBefore(
                                    stockCode,
                                    LocalDate.now().minusMonths(1)
                            )
                            .map(StockRecord::getClosePrice)
                            .orElse(0);

                    Double lastMonthChangeRate = calculateChangeRate(currentPrice, lastMonthPrice);
                    return StockRecordConverter.toMonthlyStockInfoResponse(stockCode, stockName, currentPrice, lastMonthChangeRate);
                })
                .toList();
    }

    private Double calculateChangeRate(int currentPrice, int lastMonthPrice) {
        if (lastMonthPrice == 0) return null;
        return (double) ((currentPrice - lastMonthPrice) / lastMonthPrice) * 100;
    }
}
