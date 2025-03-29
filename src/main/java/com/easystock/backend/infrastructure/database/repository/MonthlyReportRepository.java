package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
    Optional<MonthlyReport> findByMemberIdAndYearAndMonth(Long memberId, int year, int month);

    boolean existsByMemberAndYearMonth(Member member, int year, int month);
}
