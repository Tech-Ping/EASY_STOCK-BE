package com.easystock.backend.application.service.mypage;

import com.easystock.backend.application.service.stock.StockService;
import com.easystock.backend.aspect.exception.QuizException;
import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.StockRecord;
import com.easystock.backend.infrastructure.database.entity.mapping.Bookmark;
import com.easystock.backend.infrastructure.database.repository.*;
import com.easystock.backend.presentation.api.dto.converter.MemberConverter;
import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.presentation.api.dto.converter.StockRecordConverter;
import com.easystock.backend.presentation.api.dto.response.*;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import com.easystock.backend.util.DateUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

import static com.easystock.backend.util.FormatUtils.calculateChangeRate;

@Service @AllArgsConstructor
public class MyPageServiceImpl implements MyPageService {
    private final MemberRepository memberRepository;
    private final InventoryRepository inventoryRepository;
    private final StockRecordRepository stockRecordRepository;
    private final BookmarkRepository bookmarkRepository;
    private final StockService stockService;
    private final MonthlyReportRepository monthlyReportRepository;

    private static final Logger log = LoggerFactory.getLogger(MyPageServiceImpl.class);


    @Override
    public GetMemberProfileResponse getMyProfile(Long memberId){
        return memberRepository.findById(memberId)
                .map(MemberConverter::toProfileResDto)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public MonthlyReportResponse getMyMonthlyTradeReport(Long memberId, YearMonth yearMonth) {
        return monthlyReportRepository.findByMemberIdAndYearMonth(memberId, yearMonth)
                .map(MonthlyReportResponse::from)
                .orElseThrow(() -> new QuizException(ErrorStatus.REPORT_NOT_FOUND));
    }

    @Override
    public List<MonthlyStockInfoResponse> getMyCurrentStockStatus(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));

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
                                    DateUtils.getValidOneMonthAgo()
                            )
                            .map(StockRecord::getClosePrice)
                            .orElse(0);

                    Double lastMonthChangeRate = calculateChangeRate(currentPrice, lastMonthPrice);
                    return StockRecordConverter.toMonthlyStockInfoResponse(stockCode, stockName, currentPrice, lastMonthChangeRate);
                })
                .toList();
    }

    @Override
    public List<MonthlyStockInfoResponse> getMyBookmarkTickersCurrentStatus(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        List<Bookmark> bookmarks = bookmarkRepository.findAllByMember(member);

        return bookmarks.stream()
                .map(bookmark -> {
                    Long stockId = bookmark.getStock().getId();
                    String stockCode = bookmark.getStock().getCode();

                    StockPricesResponse currentPriceInfo = stockService.getStockPrice(stockId);
                    int currentPrice = Math.toIntExact(currentPriceInfo.getStckPrpr());
                    String stockName = currentPriceInfo.getStockName();

                    int lastMonthPrice = stockRecordRepository
                            .findRecentBefore(stockCode, DateUtils.getValidOneMonthAgo())
                            .map(StockRecord::getClosePrice)
                            .orElse(0);

                    Double changeRate = calculateChangeRate(currentPrice, lastMonthPrice);
                    log.info("{} 현재가: {}, 한달 전 종가: {}", stockCode, currentPrice, lastMonthPrice);
                    return StockRecordConverter.toMonthlyStockInfoResponse(
                            stockCode, stockName, currentPrice, changeRate
                    );
                })
                .toList();
    }

}
