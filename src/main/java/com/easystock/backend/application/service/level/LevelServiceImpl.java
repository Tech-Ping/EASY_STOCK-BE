package com.easystock.backend.application.service.level;

import com.easystock.backend.aspect.exception.LevelException;
import com.easystock.backend.aspect.exception.QuizException;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.converter.MemberConverter;
import com.easystock.backend.presentation.api.dto.response.LevelUpResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {
    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public LevelUpResponse levelUpIfPossible(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new QuizException(ErrorStatus.MEMBER_NOT_FOUND));
        int xpGauge = member.getXpGauge();

        boolean isTutorialCompleted = member.getIsTutorialCompleted();
        boolean isQuizCompleted = member.getIsQuizCompleted();
        LevelType currentLevel = member.getLevel();

        if(!isTutorialCompleted && !isQuizCompleted)
            throw new LevelException(ErrorStatus.LEVELUP_CONDITION_UNSATISFIED);

        LevelType nextLevel = calculateNextLevel(currentLevel, xpGauge);
        if(nextLevel == null)
            throw new LevelException(ErrorStatus.LEVELUP_CONDITION_UNSATISFIED);
        memberRepository.improveLevel(memberId, nextLevel);
        memberRepository.addRewardTokens(memberId, 300000);
        return MemberConverter.toLevelUpResDto(nextLevel);
    }

    private LevelType calculateNextLevel(LevelType currentLevel, int xpGauge) {
        return switch (currentLevel) {
            case ZERO -> xpGauge >= 50 ? LevelType.ONE : null;
            case ONE -> xpGauge >= 200 ? LevelType.TWO : null;
            case TWO -> xpGauge >= 600 ? LevelType.THREE : null;
            case THREE -> xpGauge >= 1000 ? LevelType.FOUR : null;
            case FOUR -> xpGauge >= 2000 ? LevelType.FIVE : null;
            case FIVE -> xpGauge >= 3500 ? LevelType.SIX : null;
            default -> null;
        };
    }
}
