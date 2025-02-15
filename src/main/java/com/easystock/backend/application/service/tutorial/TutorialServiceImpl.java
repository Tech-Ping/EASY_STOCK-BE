package com.easystock.backend.application.service.tutorial;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.aspect.exception.TutorialException;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.presentation.api.dto.converter.TutorialConverter;
import com.easystock.backend.presentation.api.dto.response.CompleteTutorialResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class TutorialServiceImpl implements TutorialService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public CompleteTutorialResponse completeTutorial(Long memberId){
        Member currentMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));
        LevelType currentLevel = currentMember.getLevel();
        LevelType nextLevel = calculateNextLevel(currentLevel);
        memberRepository.completeTutorialAndRewardTokens(memberId, 1000);
        return TutorialConverter.toCompleteTutorialResDto(nextLevel);
    }

    private LevelType calculateNextLevel(LevelType currentLevel) {
        return switch (currentLevel) {
            case ZERO -> LevelType.ONE;
            case ONE -> LevelType.TWO;
            case TWO -> LevelType.THREE;
            case THREE -> LevelType.FOUR;
            case FOUR -> LevelType.FIVE;
            case FIVE -> LevelType.SIX;
            default -> throw new TutorialException(ErrorStatus.TUTORIAL_UNSUPPORTED);
        };
    }
}
