/*
 * Copyright 2011-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * 业务超时判断.
 *
 * @author liym
 * @since 2021-03-30 15:39 新建
 */
public final class TimeoutTest {

    /**
     * 判断一个时间戳是否超时
     *
     * @param timeMillis 时间戳
     * @param timeoutSec 超时时间（单位秒）
     * @return true/false
     */
    private static boolean isTimeout(long timeMillis, int timeoutSec) {
        long currentTimeMs = System.currentTimeMillis();
        long costTime = currentTimeMs - timeMillis;
        return costTime > timeoutSec * 1000L;
    }

    public static void main(String[] args) {
        LocalDateTime taskTime = LocalDateTime.now(); // 任务开始时间
        int timeoutSec = 60; // 超时时间（60秒）

        // 处理业务
        try {
            TimeUnit.MINUTES.sleep(1L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        // 判断业务处理是否超时
        long epochMillis = taskTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        boolean timeout = isTimeout(epochMillis, timeoutSec);
        System.out.println(timeout);
    }

}
