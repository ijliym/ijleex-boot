/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 工具类
 *
 * @author liym
 * @since 2017-08-30 11:11 新建
 */
public final class Utils {

    public static int safeToInt(Object str) {
        String ss = String.valueOf(str);
        int val = 0;
        try {
            val = Integer.parseInt(ss);
        } catch (NumberFormatException ignored) {
            // System.err.println("safeToInt error: " + str);
        }
        return val;
    }

    /**
     * String to Array
     *
     * @param str String
     * @param delimiters 分隔符
     * @return an array of the tokens
     * @see java.util.StringTokenizer
     * @see String#trim()
     * @see org.springframework.util.StringUtils#tokenizeToStringArray(java.lang.String, java.lang.String)
     * @since 2018-05-16 13:18
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        if (str == null) {
            return new String[0];
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            token = token.trim();

            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return tokens.toArray(new String[0]);
    }

    /**
     * 计算平均值
     *
     * @param x 第一个值
     * @param y 第二个值
     * @return 平均值
     * @since 2018-04-21 17:52:28
     */
    public static int calcAverageValue(int x, int y) {
        int z = x + y;
        return z / 2;
    }

}
