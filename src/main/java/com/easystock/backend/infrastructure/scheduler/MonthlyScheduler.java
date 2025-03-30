package com.easystock.backend.infrastructure.scheduler;

import com.easystock.backend.application.service.mypage.InvestmentTypeAnalyzer;
import com.easystock.backend.infrastructure.database.entity.Member;
import com.easystock.backend.infrastructure.database.entity.MonthlyReport;
import com.easystock.backend.infrastructure.database.entity.enums.InvestmentType;
import com.easystock.backend.infrastructure.database.repository.MemberRepository;
import com.easystock.backend.infrastructure.database.repository.MonthlyReportRepository;
import com.easystock.backend.infrastructure.scheduler.collector.MonthlyGraphDataCollector;
import com.easystock.backend.infrastructure.scheduler.collector.MonthlyTopStockCollector;
import com.easystock.backend.presentation.api.dto.response.DailyProfit;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonthlyScheduler {

    private final MemberRepository memberRepository;
    private final MonthlyReportRepository monthlyReportRepository;
    private final InvestmentTypeAnalyzer investmentTypeAnalyzer;
    private final MonthlyTopStockCollector monthlyTopStockCollector;
    private final MonthlyGraphDataCollector monthlyGraphDataCollector;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(MonthlyScheduler.class);

    /**
     * 매월 1일 00시에 실행되는 매월 투자 리포트 생성 스케줄러
     */
    @Transactional
    @Scheduled(cron = "0 0 0 1 * *")
    public void generateMonthlyReports() {
        YearMonth lastMonth = YearMonth.now().minusMonths(1);
        LocalDate start = lastMonth.atDay(1);
        LocalDate end = lastMonth.atEndOfMonth();

        List<Member> members = memberRepository.findAllWithInventoriesAndStocks();

        for (Member member : members) {
            boolean exists = monthlyReportRepository.existsByMemberAndYearAndMonth(member, lastMonth.getYear(), lastMonth.getMonthValue());
            if (exists) continue;

            try {
                InvestmentType investmentType = investmentTypeAnalyzer.analyze(member.getId());
                List<MonthlyStockInfoResponse> topStocks = monthlyTopStockCollector.selectTopStock(member);
                List<DailyProfit> profitGraph = monthlyGraphDataCollector.generateDailyProfitGraph(member, start, end);

                String topStocksJson = objectMapper.writeValueAsString(topStocks);
                String profitGraphJson = objectMapper.writeValueAsString(profitGraph);

                MonthlyReport report = MonthlyReport.builder()
                        .member(member)
                        .year(lastMonth.getYear())
                        .month(lastMonth.getMonthValue())
                        .investmentType(investmentType)
                        .topStocksJson(topStocksJson)
                        .profitGraphJson(profitGraphJson)
                        .build();

                monthlyReportRepository.save(report);
                log.info("[월간 리포트 생성 완료] {} / {} - {}", member.getId(), lastMonth, investmentType);
            } catch (Exception e) {
                log.error("[월간 리포트 생성 실패] {}: {}", member.getId(), e.getMessage());
            }
        }
    }
}
