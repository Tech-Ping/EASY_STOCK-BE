package com.easystock.backend.application.service.trade;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.aspect.exception.StockException;
import com.easystock.backend.aspect.exception.TradeException;
import com.easystock.backend.infrastructure.database.entity.Inventory;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.database.entity.enums.TradeType;
import com.easystock.backend.infrastructure.database.repository.InventoryRepository;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.infrastructure.database.repository.StockRepository;
import com.easystock.backend.infrastructure.database.repository.TradeRepository;
import com.easystock.backend.presentation.api.dto.converter.InventoryConverter;
import com.easystock.backend.presentation.api.dto.converter.TradeConverter;
import com.easystock.backend.presentation.api.dto.request.TradeRequest;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import com.easystock.backend.presentation.api.dto.response.TradeResultResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final StockRepository stockRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public List<TradeResponse> getAllTradesByUser(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        return tradeRepository.findAllByCustomer(member).stream()
                .map(trade -> TradeConverter.toTradeResponse(trade))
                .toList();
    }

    @Override
    public List<TradeResponse> getTradesByStatus(Long memberId, TradeStatus status) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        return tradeRepository.findAllByCustomerAndStatus(member, status).stream()
                .map(trade -> TradeConverter.toTradeResponse(trade))
                .toList();
    }

    @Override
    @Transactional
    public TradeResultResponse createTrade(Long memberId, TradeRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        Stock stock = stockRepository.findById(request.getStockId())
                .orElseThrow(() -> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        TradeStatus tradeStatus = changeInitialTradeStatusTo(request.getTradePrice(), request.getCurrentPrice());
        Trade trade = TradeConverter.toTrade(request, member, stock, tradeStatus);
        tradeRepository.save(trade);

        if (tradeStatus == TradeStatus.COMPLETED) {
            processTrade(trade, member, stock);
        }

        return TradeConverter.toTradeResultResponse(trade, member, stock, "주문이 정상적으로 접수되었습니다.");
    }

    @Override
    @Transactional
    public TradeResultResponse cancelTrade(Long memberId, Long tradeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new TradeException(ErrorStatus.TRADE_NOT_FOUND));

        if (trade.getCustomer() != member) {
            throw new TradeException(ErrorStatus.TRADE_NOT_OWNED_BY_USER);
        }
        if (trade.getStatus() == TradeStatus.PENDING) {
            trade.cancelTrade();
        } else {
            throw new TradeException(ErrorStatus.TRADE_CANNOT_BE_CANCELLED);
        }
        return TradeConverter.toTradeResultResponse(trade, member, trade.getStock(), "주문이 정상적으로 취소되었습니다.");
    }

    public TradeStatus changeInitialTradeStatusTo(Integer tradePrice, Integer currentPrice) {
        if (Objects.equals(tradePrice, currentPrice)) {
            return TradeStatus.COMPLETED;
        } else {
            return TradeStatus.PENDING;
        }
    }

    @Override
    @Async
    @Transactional
    public void checkTradeStatus(Stock stock, Long currentPrice) {
        List<Trade> trades = tradeRepository.findTradesByStatusAndStock(TradeStatus.PENDING, stock);

        for (Trade trade : trades) {
            try {
                if (trade.getPrice() == currentPrice.intValue()){
                    validateTrade(trade, trade.getCustomer(), stock); // 거래 가능 여부 사전 검증
                    executeTrade(trade, stock);
                }
            } catch (TradeException e) {
                log.info("거래 처리 실패: tradeId={}, 이유={}", trade.getId(), e.getMessage());
            }
        }
    }

    private void validateTrade(Trade trade, Member member, Stock stock) {
        int totalPrice = trade.getPrice() * trade.getQuantity();

        if (trade.getType() == TradeType.BUY && member.getTokenBudget() < totalPrice) {
            throw new TradeException(ErrorStatus.INSUFFICIENT_FUNDS);
        }

        if (trade.getType() == TradeType.SELL &&
                inventoryRepository.findStockQuantityByMemberAndStock(member, stock) < trade.getQuantity()) {
            throw new TradeException(ErrorStatus.INSUFFICIENT_STOCKS);
        }
    }

    private void executeTrade(Trade trade, Stock stock) {
        trade.completeTrade();
        tradeRepository.save(trade);
        processTrade(trade, trade.getCustomer(), stock);
    }

    private void processTrade(Trade trade, Member member, Stock stock) {
        if (trade.getType() == TradeType.BUY) {
            processBuyTrade(trade, member, stock);
        } else {
            processSellTrade(trade, member, stock);
        }
    }

    private void processSellTrade(Trade trade, Member member, Stock stock) {
        int totalPrice = trade.getPrice() * trade.getQuantity();

        // 기존 재고 업데이트
        Inventory inventory = inventoryRepository.findByMemberAndStock(member, stock)
                .orElseThrow(() -> new TradeException(ErrorStatus.INSUFFICIENT_STOCKS));
        inventory.updateQuantity(-trade.getQuantity());
        inventory.updateTotalPrice(-totalPrice);

        // 예산 증가
        member.updateTokenBudget(totalPrice);
    }

    private void processBuyTrade(Trade trade, Member member, Stock stock) {
        int totalPrice = trade.getPrice() * trade.getQuantity();

        // 예산 감소
        member.updateTokenBudget(-totalPrice);

        // 기존 재고가 있으면 업데이트, 없으면 새로 생성
        inventoryRepository.findByMemberAndStock(member, stock)
                .ifPresentOrElse(
                        inventory -> {
                            inventory.updateQuantity(trade.getQuantity());
                            inventory.updateTotalPrice(totalPrice);
                        },
                        () -> inventoryRepository.save(InventoryConverter.toInventory(trade.getQuantity(), totalPrice, member, stock))
                );
    }
}

