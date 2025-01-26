package com.easystock.backend.application.service.quiz;

import com.easystock.backend.infrastructure.database.entity.Quiz;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.presentation.api.dto.response.QuizSubmitResponse;

import java.util.List;

public interface QuizService {
    Quiz getQuizProblem(Long memberId);
    QuizSubmitResponse submitAnswer(Long memberId, Long quizId, int inputIndex);
}
