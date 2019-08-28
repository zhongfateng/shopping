package com.liuwa.shopping.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by ZFT on 2019/8/22.
 */

public class MoneyUtils {

    /**
     * 金额0.00。
     */
    public static final BigDecimal ZERO = BigDecimal.valueOf(0.00);

    /**
     * 金额100.00。
     */
    public static final BigDecimal HUNDRED = new BigDecimal(100.00);

    private MoneyUtils() {
        super();
    }

    /**
     * 四舍五入保留1位小数（四舍六入五成双）。
     *
     * @param amount 金额。
     * @return  四舍五入保留1位小数后金额。
     */
    public static BigDecimal decimal1ByUp(BigDecimal amount) {
        return amount.setScale(1, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 四舍五入保留2位小数（四舍六入五成双）。
     *
     * @param amount 金额。
     * @return  四舍五入保留2位小数后金额。
     */
    public static BigDecimal decimal2ByUp(BigDecimal amount) {
        return amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 四舍五入保留4位小数（四舍六入五成双）。
     *
     * @param amount 金额。
     * @return 四舍五入保留4位小数后金额。
     */
    public static BigDecimal decimal4ByUp(BigDecimal amount) {
        return amount.setScale(4, BigDecimal.ROUND_HALF_EVEN);
    }


    /**
     * 除法，四舍五入保留2位小数。
     *
     * @param divideAmount 金额。
     *  @param dividedAmount 被除的金额。
     * @return  四舍五入保留2位小数后金额。
     */
    public static BigDecimal divide2ByUp(BigDecimal divideAmount, BigDecimal dividedAmount) {
        return divideAmount.divide(dividedAmount,2, BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * 除法，四舍五入保留4位小数。
     *
     * @param divideAmount 金额。
     *  @param dividedAmount 被除的金额。
     * @return  四舍五入保留2位小数后金额。
     */
    public static BigDecimal divide4ByUp(BigDecimal divideAmount, BigDecimal dividedAmount) {
        return divideAmount.divide(dividedAmount,4, BigDecimal.ROUND_HALF_EVEN);
    }


    /**
     * 是否大于0。
     *
     * @param amount 金额。
     * @return true：大于0；false；小于等于0。
     */
    public static boolean isGreaterThanZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 1;
    }

    /**
     * 是否小于0。
     *
     * @param amount 金额。
     * @return true：小于0；false：大于等于0。
     */
    public static boolean isLessThanZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == -1;
    }

    /**
     * 是否等于0。
     *
     * @param amount 金额。
     * @return true：等于0；false：大于小于0。
     */
    public static boolean isEqualZero(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }


    /**
     * 第一个是否大于第二个。
     *
     * @param first  金额。
     * @param second 金额。
     * @return true：first>second；false：first<=second。
     */
    public static boolean isGreaterThan(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 1;
    }

    /**
     * 第一个是否小于第二个。
     *
     * @param first  金额。
     * @param second 金额。
     * @return true：first<second；false：first >=second。
     */
    public static boolean isLessThan(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == -1;
    }

    /**
     * 第一个是否等于第二个。
     *
     * @param first  金额。
     * @param second 金额。
     * @return true：first=second；false：first ><second。
     */
    public static boolean isEqual(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) == 0;
    }

    public static  BigDecimal POINT_BILLION = new BigDecimal("100000000");
    public static  BigDecimal TEN_THOUSANDS = new BigDecimal("10000");

    /**
     * 金额格式化为,###.00的金额,即保留两位小数。
     *
     * @param amount 需要显示成字符传,###.###的金额。
     * @return  返回String，格式为,###.###。
     */
    public static String formatAmountAsString(BigDecimal amount) {
        return amount != null ? new DecimalFormat(",##0.00").format(amount) : "0.00";
    }

    /**
     * 将格式化的字符串金额反转为{@link BigDecimal}。
     *
     * @param amount 格式为,###.###。
     * @return {@link java.math.BigDecimal}。
     */
    public static BigDecimal parseAmount(String amount) {
        return new BigDecimal(amount.replaceAll(",", ""));
    }
}