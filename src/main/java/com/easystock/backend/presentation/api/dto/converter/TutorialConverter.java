package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.presentation.api.dto.response.CompleteTutorialResponse;
import org.springframework.stereotype.Component;

@Component
public class TutorialConverter {
    public static CompleteTutorialResponse toCompleteTutorialResDto(LevelType nextLevel){
        return CompleteTutorialResponse.builder()
                .message("성공적으로 튜토리얼을 마쳤습니다. 100만 STOKEN을 보상으로 드려요!")
                .build();
    }
}
