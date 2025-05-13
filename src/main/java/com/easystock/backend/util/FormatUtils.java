package com.easystock.backend.util;

import com.easystock.backend.infrastructure.database.entity.Inventory;

public class FormatUtils {

    /**
     * 변화율(Double)을 +기호와 % 포함한 문자열로 포맷합니다.
     * 예: 10.46 → "+10.46%", -3.21 → "-3.21%", 0.00 → "0.00%"
     */
    public static String formatRatePercentage(Double rate) {
        if (rate == null) return "-";

        // 소수점 둘째 자리까지 반올림
        double rounded = Math.round(rate * 100) / 100.0;

        // 부호 판별 및 포맷팅
        if (rounded > 0) {
            return "+" + rounded + "%";
        } else if (rounded < 0) {
            return rounded + "%";  // 음수는 자동으로 -가 붙음
        } else {
            return "0.00%"; // 0일 때는 부호 없이
        }
    }


    /**
     * 변화율을 계산합니다.
     */
    public static Double calculateChangeRate(int currentPrice, int lastPrice) {
        if (lastPrice == 0) return 0.0;

        double rawRate = (currentPrice - lastPrice) * 100.0 / lastPrice;
        return Math.round(rawRate * 100) / 100.0;
    }

    /**
     * 개별 보유 종목의 총 투자금 계산
     */
    public static int calculateTotalInvestedAmount(Inventory inventory) {
        if (inventory.getQuantity() == 0) return 0;
        return inventory.getTotalPrice();
    }

    /**
     * 평균 매수가 계산
     */
    public static int calculateAveragePurchasePrice(Inventory inventory) {
        if (inventory.getQuantity() == 0) return 0;
        return inventory.getTotalPrice() / inventory.getQuantity();
    }
}
