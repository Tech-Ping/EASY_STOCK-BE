package com.easystock.backend.infrastructure.database.entity.mapping;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.Quiz;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @NoArgsConstructor
public class MemberQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    public MemberQuiz(Member member, Quiz quiz, boolean completed) {
        this.member = member;
        this.quiz = quiz;
        this.completed = completed;
    }
}
