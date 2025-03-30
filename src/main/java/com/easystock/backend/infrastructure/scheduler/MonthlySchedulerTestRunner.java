package com.easystock.backend.infrastructure.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthlySchedulerTestRunner {
    private final MonthlyScheduler monthlyScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void runOnStartup() {
        monthlyScheduler.generateMonthlyReports();
    }
}
