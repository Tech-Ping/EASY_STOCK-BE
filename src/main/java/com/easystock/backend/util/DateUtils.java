package com.easystock.backend.util;

import java.time.LocalDate;

public class DateUtils {

    /**
     * 오늘 날짜 기준으로 한 달 전의 유효한 날짜를 반환합니다.
     * (예: 3월 31일 → 2월 28일 or 29일)
     */
    public static LocalDate getValidOneMonthAgo() {
        LocalDate now = LocalDate.now();
        LocalDate oneMonthAgo = now.minusMonths(1);
        int day = Math.min(now.getDayOfMonth(), oneMonthAgo.lengthOfMonth());
        return oneMonthAgo.withDayOfMonth(day);
    }

    /**
     * 기준 날짜 기준으로 한 달 전의 유효한 날짜를 반환합니다.
     */
    public static LocalDate getValidOneMonthAgoDate(LocalDate date) {
        LocalDate oneMonthAgo = date.minusMonths(1);
        int day = Math.min(date.getDayOfMonth(), oneMonthAgo.lengthOfMonth());
        return oneMonthAgo.withDayOfMonth(day);
    }
}
