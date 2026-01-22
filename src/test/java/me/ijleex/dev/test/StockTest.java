/*
 * Copyright 2011-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2025年，怎么通过A股从￥10000赚到￥33亿？
 *
 * @author liym
 * @since 2025-12-31 19:19 新建
 */
public final class StockTest {

    /**
     * {@code 1 ÷ 100.0}
     */
    private static final BigDecimal INVERSE_HUNDRED = new BigDecimal("0.01");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private StockTest() {
    }

    /**
     * 终值计算
     *
     * @see <a href="https://www.calculator.net/future-value-calculator.html">Future Value Calculator</a>
     */
    @Test
    public void futureValue() {
        // 每月涨幅
        List<? extends Number> incrRates = List.of(
                84.49d,   //  1月，冀东装备
                226.65d,  //  2月，万达轴承
                135.41d,  //  3月，中毅达
                159.99d,  //  4月，联合化学
                147.28d,  //  5月，中邮科技
                159.97d,  //  6月，北方长龙
                1083.42d, //  7月，上纬新材！！！
                147.90d,  //  8月，开普云
                181.20d,  //  9月，首开股份
                107.49d,  // 10月，海峡创新
                155.85d,  // 11月，国晟科技
                223.39d); // 12月，胜通能源

        double x = 10000.0d; // 本金，1万
        logger.info(" 本金：{}", x);
        String n = Double.toString(x);
        for (int i = 0, s = incrRates.size(); i < s; i++) {
            Number incrRate = incrRates.get(i);
            n = calcMarketValue(n, incrRate.toString());
            logger.info("{}月：{}", StringUtils.leftPad(Integer.toString(i + 1), 2), n);
        }
    }

    /**
     * 根据本金与涨幅计算股票盈亏，并得到市值
     *
     * <p>{@code principal * (1 + incrRate / 100)}</p>
     *
     * @param principal 本金
     * @param incrRate 涨幅（不含百分比）
     * @return 股票市值：{@code principal + (principal * incrRate / 100)}
     * @since 2025-12-31 19:51
     */
    private String calcMarketValue(String principal, String incrRate) {
        BigDecimal n = new BigDecimal(principal);
        BigDecimal incrFactor = BigDecimal.ONE.add(new BigDecimal(incrRate).multiply(INVERSE_HUNDRED)); // 增长因子（如 1.05 表示增长 5%）
        // logger.info("增长因子：{}", incrFactor);
        return n.multiply(incrFactor).toPlainString();
    }

    /**
     * 涨幅计算
     *
     * <p>{@code Growth Rate (%) = (Ending Value ÷ Beginning Value) – 1}</p>
     *
     * @since 2026-01-20 21:20
     */
    @Test
    public void incrRate() {
        double originalValue = 80000.0d, newVal = 100000.0d;
        double incrRate = (newVal / originalValue) - 1.0d;
        logger.info("涨幅：{}%", incrRate * 100.0d);
    }

}
