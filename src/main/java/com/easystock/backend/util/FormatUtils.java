package com.easystock.backend.util;

public class FormatUtils {

    /**
     * 변화율(Double)을 +기호와 % 포함한 문자열로 포맷합니다.
     * 예: 10.46 → "+10.46%", -3.21 → "-3.21%"
     */

    public static String formatRatePercentage(Double rate) {
        if (rate == null) return "-";

        // 소수점 둘째 자리까지 반올림
        double rounded = Math.round(rate * 100) / 100.0;

        // 부호 판별 및 포맷팅
        String sign = rounded > 0 ? "+" : ""; // 음수는 자동으로 - 붙음
        return sign + rounded + "%";
    }
}
