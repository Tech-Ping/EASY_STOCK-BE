package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity @Getter
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", nullable = false)
    private String question;

    @ElementCollection
    @CollectionTable(name = "QUIZ_OPTIONS", joinColumns = @JoinColumn(name = "quiz_id"))
    @Column(name = "options", nullable = false)
    private List<String> options;

    @Column(name = "answer_index", nullable = false)
    private int answerIndex;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private LevelType levelType;
}
