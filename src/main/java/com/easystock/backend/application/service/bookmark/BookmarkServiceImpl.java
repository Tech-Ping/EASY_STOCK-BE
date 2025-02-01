package com.easystock.backend.application.service.bookmark;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.aspect.exception.StockException;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Stock;
import com.easystock.backend.infrastructure.database.entity.mapping.Bookmark;
import com.easystock.backend.infrastructure.database.repository.BookmarkRepository;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.infrastructure.database.repository.StockRepository;
import com.easystock.backend.presentation.api.dto.converter.BookmarkConverter;
import com.easystock.backend.presentation.api.dto.response.BookmarkResponse;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final StockRepository stockRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public BookmarkResponse registerOrUnregister(Long memberId, Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(()-> new StockException(ErrorStatus.STOCK_NOT_FOUND));

        Optional<Bookmark> existingBookmark = bookmarkRepository.findByMemberIdAndStockId(memberId, stockId);
        if(existingBookmark.isPresent()){
            bookmarkRepository.deleteByMemberIdAndStockId(memberId, stock.getId());
            return BookmarkConverter.toBookmarkResDto(stock, false);
        }
        else {
            Member currentMember = memberRepository.findById(memberId)
                    .orElseThrow(()-> new AuthException(ErrorStatus.MEMBER_NOT_FOUND));

            Bookmark newBookmark = BookmarkConverter.toBookmark(currentMember, stock);
            bookmarkRepository.save(newBookmark);
            return BookmarkConverter.toBookmarkResDto(stock, true);
        }
    }
}
