package com.easystock.backend.application.service.mock;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.aspect.exception.QuizException;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.request.StokenRequest;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MockServiceImpl implements MockService {

    private final MemberRepository memberRepository;

    @Transactional
    public void acquireStoken(Long memberId, StokenRequest stokenRequest){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        memberRepository.addRewardTokens(memberId, member.getTokenBudget() + stokenRequest.getStoken());
    }
}
