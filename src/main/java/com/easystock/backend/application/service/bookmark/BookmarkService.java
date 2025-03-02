package com.easystock.backend.application.service.bookmark;

import com.easystock.backend.presentation.api.dto.response.BookmarkResponse;

public interface BookmarkService {
    BookmarkResponse registerOrUnregister(Long memberId, Long stockId);
}
