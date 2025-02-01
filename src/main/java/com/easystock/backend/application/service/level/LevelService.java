package com.easystock.backend.application.service.level;

import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.presentation.api.dto.response.LevelUpResponse;

public interface LevelService {
    LevelUpResponse levelUpIfPossible(Long memberId);
}
