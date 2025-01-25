package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.quiz.QuizService;
import com.easystock.backend.infrastructure.database.entity.Quiz;
import com.easystock.backend.presentation.api.dto.request.QuizSubmitRequest;
import com.easystock.backend.presentation.api.dto.response.QuizSubmitResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/quiz")
@Tag(name = "퀴즈 API - /api/quiz ")
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/problem-solve")
    @Operation(
            summary = "퀴즈 조회 API - 유저가 레벨에 따른 퀴즈 정보를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<Quiz> getQuizProblems(
            @Parameter(hidden = true)
            @AuthUser Long memberId) {
        return ApiResponse.onSuccess(quizService.getQuizProblem(memberId));
    }

    @PostMapping("/{quizId}/submit")
    @Operation(
            summary = "퀴즈 제출 API - 유저가 퀴즈에 대한 문제를 제출합니다.",
            description = "유저가 풀고자 하는 퀴즈의 ID와 입력한 정답 인덱스를 요청 바디로 제출합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<QuizSubmitResponse> submitQuizAnswer(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @RequestParam Long quizId,
            @RequestBody @Valid QuizSubmitRequest request) {
        return ApiResponse.onSuccess(quizService.submitAnswer(memberId, quizId, request.getInputIndex()));
    }

}
