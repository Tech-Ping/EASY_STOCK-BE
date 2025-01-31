package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);
    Member findByUsername(String username);

    @Modifying
    @Query("UPDATE Member m SET m.xpGauge = m.xpGauge + :xp WHERE m.id = :memberId")
    void addXpGauge(@Param("memberId") Long memberId, @Param("xp") int xp);

    @Modifying
    @Query("UPDATE Member m SET m.level = :nextLevel WHERE m.id = :memberId")
    void improveLevel(@Param("memberId") Long memberId, @Param("nextLevel")LevelType nextLevel);

    @Modifying
    @Query("UPDATE Member m SET m.isTutorialCompleted = true, m.tokenBudget = m.tokenBudget + :rewardTokens WHERE m.id = :memberId")
    void completeTutorialAndRewardTokens(@Param("memberId") Long memberId, @Param("rewardTokens") int rewardTokens);
}
