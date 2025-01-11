package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.common.AuditingEntity;
import com.easystock.backend.infrastructure.database.entity.enums.LevelType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @Builder
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

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private LevelType level;

    @Column(name = "is_agreed", nullable = false)
    private Boolean isAgreed;

}
