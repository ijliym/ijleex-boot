/*
 * Copyright 2011-2018 ijym-lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.ijleex.dev.test.inputmethod;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 工具类
 *
 * @author ijymLee
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
    public static String calcAverageValue(String x, String y) {
        int v1 = safeToInt(x);
        int v2 = safeToInt(y);
        int z = v1 + v2;
        return String.valueOf(z / 2);
    }

    public static void main(String[] args) {
        String ss = "$ddcmd(config(/do 输出反查),[反查]：<last.1>)\tlookup#序4096";
        String[] data = Utils.tokenizeToStringArray(ss, "\t#"); // \t #
        for (String s : data) {
            System.out.println(s);
        }
    }

}
