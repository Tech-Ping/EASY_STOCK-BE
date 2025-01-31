package com.easystock.backend.application.service.trade;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.aspect.exception.StockException;
import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.database.repository.InventoryRepository;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.infrastructure.database.repository.StockRepository;
import com.easystock.backend.infrastructure.database.repository.TradeRepository;
import com.easystock.backend.presentation.api.dto.converter.InventoryConverter;
import com.easystock.backend.presentation.api.dto.converter.QuizConverter;
import com.easystock.backend.presentation.api.dto.converter.TradeConverter;
import com.easystock.backend.presentation.api.dto.request.TradeRequest;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResultResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;
    private final InventoryRepository inventoryRepository;

    public List<TradeResponse> getAllTradesByUser(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        return tradeRepository.findAllByCustomer(member).stream()
                .map(trade -> TradeConverter.toTradeResponse(trade))
                .toList();
    }

    public List<TradeResponse> getTradesByStatus(Long memberId, TradeStatus status) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        return tradeRepository.findAllByCustomerAndStatus(member, status).stream()
                .map(trade -> TradeConverter.toTradeResponse(trade))
                .toList();
    }

    @Transactional
    public TradeResultResponse createTrade(Long memberId, TradeRequest request){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        TradeStatus tradeStatus = changeTradeStatusTo(request.getTradePrice(), request.getCurrentPrice());
        Trade trade = TradeConverter.toTrade(request, member, stock, tradeStatus);
        tradeRepository.save(trade);

        if (tradeStatus == TradeStatus.COMPLETED){
            createInventory(trade, member, stock);
        }

        return TradeConverter.toTradeResultResponse(trade, member, stock);
    }

    // to-do: 중복 생성 안 되도록 주의하기
    public void createInventory(Trade trade, Member member, Stock stock){
        Inventory inventory = InventoryConverter.toInventory(trade, member, stock);
        inventoryRepository.save(inventory);
    }

    public TradeStatus changeTradeStatusTo(Integer tradePrice, Integer currentPrice){
        if (Objects.equals(tradePrice, currentPrice)){
            return TradeStatus.COMPLETED;
        }
        else {
            return TradeStatus.PENDING;
        }
    }
}
