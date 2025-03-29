package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.mapping.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByMemberIdAndStockId(Long memberId, Long stockId);
    void deleteByMemberIdAndStockId(Long memberId, Long stockId);

    List<Bookmark> findAllByMember(Member member);
}
