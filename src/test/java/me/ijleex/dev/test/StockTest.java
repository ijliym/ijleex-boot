/*
 * Copyright 2011-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 2025年，怎么通过A股从￥10000赚到￥33亿？
 *
 * @author liym
 * @since 2025-12-31 19:19 新建
 */
public final class StockTest {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.0");

    private StockTest() {
    }

    public static void main(String[] args) {
        // 每月涨幅
        List<? extends Number> incrRates = List.of(
                84.49d,   //  1月，冀东装备
                226.65d,  //  2月，万达轴承
                135.41d,  //  3月，中毅达
                159.99f,  //  4月，联合化学
                147.28d,  //  5月，中邮科技
                159.97d,  //  6月，北方长龙
                1083.42d, //  7月，上纬新材！！！
                147.9d,   //  8月，开普云
                181.2d,   //  9月，首开股份
                107.49d,  // 10月，海峡创新
                155.85d,  // 11月，国晟科技
                223.39d); // 12月，胜通能源

        double x = 10000.0d; // 本金，1万
        String n = Double.toString(x);
        System.out.println(" 本金：" + n);
        for (int i = 0, s = incrRates.size(); i < s; i++) {
            Number incrRate = incrRates.get(i);
            n = calcMarketValue(n, incrRate.toString());
            System.out.println(StringUtils.leftPad(Integer.toString(i + 1), 2) + "月：" + n);
        }
    }

    /**
     * 根据本金与涨幅计算股票盈亏，并得到市值
     *
     * @param principal 本金
     * @param incrRate 涨幅（不含百分比）
     * @return 股票市值：{@code principal + (principal * incrRate / 100)}
     * @since 2025-12-31 19:51
     */
    private static String calcMarketValue(String principal, String incrRate) {
        BigDecimal n = new BigDecimal(principal);
        BigDecimal profit = n.multiply(new BigDecimal(incrRate)).divide(ONE_HUNDRED, RoundingMode.HALF_EVEN); // 盈亏金额
        return n.add(profit).toPlainString();
    }

}
