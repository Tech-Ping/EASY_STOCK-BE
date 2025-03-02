package com.easystock.backend.presentation.api.dto.converter;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.mapping.Bookmark;
import com.easystock.backend.presentation.api.dto.response.BookmarkResponse;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component @Builder
public class BookmarkConverter {
    public static BookmarkResponse toBookmarkResDto(Stock stock, boolean isBookmarked){
        String message = isBookmarked
                ? "북마크가 성공적으로 등록되었습니다."
                : "북마크가 성공적으로 해제되었습니다.";
        return BookmarkResponse.builder()
                .stockName(stock.getName())
                .stockCode(stock.getCode())
                .isBookmarked(isBookmarked)
                .message(message)
                .build();
    }

    public static Bookmark toBookmark(Member member, Stock stock){
        return Bookmark.builder()
                .member(member)
                .stock(stock)
                .build();
    }
}
