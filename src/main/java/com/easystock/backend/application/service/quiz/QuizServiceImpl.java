package com.easystock.backend.application.service.quiz;

import com.easystock.backend.aspect.exception.QuizException;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Quiz;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.infrastructure.database.entity.mapping.MemberQuiz;
import com.easystock.backend.infrastructure.database.repository.MemberQuizRepository;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.infrastructure.database.repository.QuizRepository;
import com.easystock.backend.presentation.api.dto.converter.QuizConverter;
import com.easystock.backend.presentation.api.dto.response.QuizSubmitResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class QuizServiceImpl implements QuizService{
    private final MemberRepository memberRepository;
    private final QuizRepository quizRepository;
    private final MemberQuizRepository memberQuizRepository;

    @Override
    @Transactional
    public Quiz getQuizProblem(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(()-> new QuizException(ErrorStatus.MEMBER_NOT_FOUND));
        LevelType levelType = member.getLevel();
        List<Quiz> quizzes = memberQuizRepository.findUncompletedQuizProblemsByLevel(memberId, levelType);

        if(quizzes.isEmpty())
            throw new QuizException(ErrorStatus.QUIZ_NOT_FOUND);

        Quiz nextQuiz= quizzes.get(0);
        boolean exists = memberQuizRepository.existsByMemberIdAndQuizId(memberId, nextQuiz.getId());
        if (!exists) {
            MemberQuiz memberQuiz = new MemberQuiz(member, nextQuiz, false);
            memberQuizRepository.save(memberQuiz);
        }
        return nextQuiz;
    }

    @Override
    @Transactional
    public QuizSubmitResponse submitAnswer(Long memberId, Long quizId, int inputIndex) {
        MemberQuiz memberQuiz = memberQuizRepository.findByMemberIdAndQuizId(memberId, quizId);

        if (memberQuiz.isCompleted()) {
            int totalXp = memberRepository.findById(memberId).orElseThrow().getXpGauge();
            return QuizConverter.toQuizSubmitResDto(false, false, 0, totalXp);
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ErrorStatus.QUIZ_NOT_FOUND));

        boolean isCorrect = quiz.getAnswerIndex() == inputIndex;
        int addedXp = isCorrect ? 30 : 0;

        if (isCorrect) {
            memberRepository.addXpGauge(memberId, addedXp);
            memberQuizRepository.updateQuizAsCompleted(memberId, quizId);
        }

        int totalXp = memberRepository.findById(memberId)
                .orElseThrow(() -> new QuizException(ErrorStatus.MEMBER_NOT_FOUND))
                .getXpGauge();

        boolean allQuizProblemsSolved = false;
        try {
            getQuizProblem(memberId);
        } catch (QuizException e){
            if(e.getErrorStatus() == ErrorStatus.QUIZ_NOT_FOUND) {
                allQuizProblemsSolved = true;
            } else {
                throw e;
            }
        }
        if(allQuizProblemsSolved){
            memberRepository.updateQuizStatus(allQuizProblemsSolved);
        }
        return QuizConverter.toQuizSubmitResDto(isCorrect, allQuizProblemsSolved, addedXp, totalXp);
    }

}
