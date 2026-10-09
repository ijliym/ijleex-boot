/*
 * Copyright © 2011-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务超时判断.
 *
 * @author liym
 * @since 2021-03-30 15:39 新建
 */
public final class TimeoutTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private TimeoutTest() {
    }

    /**
     * 判断一个时间戳是否超时
     *
     * @param timeMillis 时间戳
     * @param timeoutSec 超时时间（单位秒）
     * @return true/false
     */
    private boolean isTimeout(long timeMillis, int timeoutSec) {
        long currentTimeMs = System.currentTimeMillis();
        long costTime = currentTimeMs - timeMillis;
        return costTime > timeoutSec * 1000L;
    }

    /**
     * 测试超时判断
     *
     * @since 2021-03-30 15:39
     */
    @Test
    public void timeout() {
        LocalDateTime taskTime = LocalDateTime.now(); // 任务开始时间
        int timeoutSec = 60; // 超时时间（60秒）

        // 处理业务
        try {
            logger.info("处理业务……");
            TimeUnit.MINUTES.sleep(1L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("处理业务中断", e);
        }

        // 判断业务处理是否超时
        long epochMillis = taskTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        boolean timeout = this.isTimeout(epochMillis, timeoutSec);
        logger.info("是否超时：{}", timeout);
    }

}
