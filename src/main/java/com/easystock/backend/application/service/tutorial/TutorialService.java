package com.easystock.backend.application.service.tutorial;


import com.easystock.backend.presentation.api.dto.response.CompleteTutorialResponse;

public interface TutorialService {
    CompleteTutorialResponse completeTutorial(Long memberId);
}
