package com.easystock.backend.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    private static final DateTimeFormatter KOREAN_YEAR_MONTH_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy년 M월", Locale.KOREAN);
    private static final DateTimeFormatter DOT_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy.MM.dd");

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

    /**
     * 기준 연월 기준으로 YYYY년 mm월로 포맷하여 반환합니다.
     */
    public static String formatYearMonth(YearMonth yearMonth) {
        return yearMonth.format(KOREAN_YEAR_MONTH_FORMATTER);
    }

    /**
     * 주어진 LocalDate를 "YYYY.MM.dd" 형식의 문자열로 포맷합니다.
     */
    public static String formatDateWithDot(LocalDate date) {
        return date.format(DOT_DATE_FORMATTER);
    }
}
