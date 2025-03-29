package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.enums.InvestmentType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "monthly_report")
public class MonthlyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "monthly_report_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private int year;

    private int month;

    @Enumerated(EnumType.STRING)
    private InvestmentType investmentType;
}
