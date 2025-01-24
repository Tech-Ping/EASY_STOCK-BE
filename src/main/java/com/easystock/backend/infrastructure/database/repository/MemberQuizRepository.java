package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Quiz;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.infrastructure.database.entity.mapping.MemberQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {
    Optional<MemberQuiz> findByMemberIdAndQuizId(Long memberId, Long quizId);

    @Query("SELECT q FROM Quiz q " +
            "WHERE q.levelType = :levelType " +
            "AND q.id NOT IN (SELECT mq.quiz.id FROM MemberQuiz mq WHERE mq.member.id = :memberId AND mq.completed = true)")
    List<Quiz> findUncompletedQuizProblemsByLevel(@Param("memberId") Long memberId, @Param("levelType") LevelType levelType);

    @Modifying
    @Query("UPDATE MemberQuiz mq SET mq.completed = true WHERE mq.member.id = :memberId AND mq.quiz.id = :quizId")
    void updateQuizAsCompleted(@Param("memberId") Long memberId, @Param("quizId") Long quizId);
}
