package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.common.AuditingEntity;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import com.easystock.backend.infrastructure.database.entity.enums.TradeStatus;
import com.easystock.backend.infrastructure.database.entity.mapping.MemberQuiz;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "MEMBER")
public class Member extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "username",nullable = false, length = 20)
    private String username;

    @Column(name= "password", nullable = false)
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "birth_date",nullable = false)
    private LocalDate birthDate;

    @Column(name = "xp_gauge", nullable = false)
    private Integer xpGauge;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private LevelType level;

    @Column(name = "is_tutorial_completed", nullable = false)
    private Boolean isTutorialCompleted;

    @Column(name = "is_quiz_completed", nullable = false)
    private Boolean isQuizCompleted;

    @Column(name = "is_agreed", nullable = false)
    private Boolean isAgreed;

    @Column(name = "token_budget", nullable = false)
    private Integer tokenBudget;

    @Column(name = "profile_image")
    private Integer profileImage;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberQuiz> memberQuizzes = new ArrayList<>();

    public void updateTokenBudget(Integer token){
        this.tokenBudget += token;
    }
}
