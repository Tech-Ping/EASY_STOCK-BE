package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.presentation.api.dto.response.QuizSubmitResponse;
import org.springframework.stereotype.Component;

@Component
public class QuizConverter {
    public static QuizSubmitResponse toQuizSubmitResDto(boolean isCorrect, boolean allQuizProblemsSolved, int addedXp, int totalXp) {
        String message = isCorrect
                ? "정답입니다! " + addedXp + " 경험치가 추가되었습니다."
                : "오답입니다. 다시 꼼꼼하게 읽고 풀어보세요!";
        return QuizSubmitResponse.builder()
                .allQuizProblemsSolved(allQuizProblemsSolved)
                .message(message)
                .isCorrect(isCorrect)
                .addedXp(addedXp)
                .totalXp(totalXp)
                .build();
    }
}
