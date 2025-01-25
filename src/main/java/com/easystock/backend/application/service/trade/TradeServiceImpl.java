package com.easystock.backend.application.service.trade;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Trade;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.infrastructure.database.repository.TradeRepository;
import com.easystock.backend.presentation.api.dto.converter.TradeConverter;
import com.easystock.backend.presentation.api.dto.response.TradeResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;

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
}
