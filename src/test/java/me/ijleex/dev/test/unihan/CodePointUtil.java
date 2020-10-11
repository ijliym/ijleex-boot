/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.unihan;

import java.util.OptionalInt;
import java.util.stream.IntStream;

import com.ibm.icu.text.UnicodeSet;

/**
 * 计算字符的 Unicode 编码
 * <p>代码点（Code Point）是指Unicode中为字符分配的编号，一个字符只占一个代码点。</p>
 *
 * @author liym
 * @since 2017-09-07 11:07 新建
 */
public final class CodePointUtil {

    /**
     * 计算字符的 Unicode 编码值
     *
     * @param ch 字符
     * @return 字符的 Unicode 编码，十进制
     * @since 2017-09-07 10:45
     */
    public static int getCodePoint(String ch) {
        if (ch == null || ch.trim().isEmpty()) {
            return -1;
        }
        IntStream intStream = ch.codePoints();
        OptionalInt firstInt = intStream.findFirst();
        return firstInt.isPresent() ? firstInt.getAsInt() : 0;
    }

    /**
     * 将十进制数值转换为十六进制
     *
     * @param codePoint 十进制数值
     * @return 十六进制
     * @since 2017-09-07 10:52
     */
    public static String intAsHex(int codePoint) {
        return Integer.toHexString(codePoint);
    }

    /**
     * 返回字符个数
     *
     * @param ch 字符串
     * @return 数量
     * @since 2018-04-21 17:17
     */
    public static long getCharsCount(String ch) {
        if (ch == null || ch.trim().isEmpty()) {
            return -1;
        }
        IntStream intStream = ch.codePoints();
        return intStream.count();
    }

    /**
     * 判断一个字符是否属于<b>中日韩统一表意文字</b>
     *
     * @param ch 字符
     * @return 字符是 CJK 统一汉字，返回 true，否则，返回 false
     * @since 2017-09-21 16:34:18
     */
    public static boolean isCJKUnifiedIdeographicChar(String ch) {
        int codePoint = CodePointUtil.getCodePoint(ch);
        if (codePoint <= 0) {
            return false;
        }

        UnicodeSet unicodeSet = buildCJKUnifiedIdeographsSet();

        // int size = unicodeSet.size(); // 87,887
        // System.out.println("中日韩统一表意文字 字数：" + size);

        return unicodeSet.contains(codePoint);
    }

    /**
     * 构造中日韩统一表意文字集合
     * <a href="https://en.wikipedia.org/wiki/Template:CJK_ideographs_in_Unicode">CJK ideographs in Unicode</a>
     *
     * @return {@link UnicodeSet}
     * @since 2019-08-25 20:54
     */
    public static UnicodeSet buildCJKUnifiedIdeographsSet() {
        UnicodeSet unicodeSet = new UnicodeSet();

        // CJK Unified Ideographs (中日韩统一表意文字)
        unicodeSet.add(0x4E00, 0x9FEF); // 20,976

        // CJK Unified Ideographs Extension A (中日韩统一表意文字扩展A)
        unicodeSet.add(0x3400, 0x4DB5); // 6,582

        // CJK Unified Ideographs Extension B
        unicodeSet.add(0x20000, 0x2A6D6); // 42,711

        // CJK Unified Ideographs Extension C
        unicodeSet.add(0x2A700, 0x2B734); // 4,149

        // CJK Unified Ideographs Extension D
        unicodeSet.add(0x2B740, 0x2B81D); // 222

        // CJK Unified Ideographs Extension E
        unicodeSet.add(0x2B820, 0x2CEA1); // 5,762

        // CJK Unified Ideographs Extension F
        unicodeSet.add(0x2CEB0, 0x2EBE0); // 7,473

        // CJK Compatibility Ideographs (中日韩兼容表意文字)
        unicodeSet.add(0xFA0E); // 12 Unified
        unicodeSet.add(0xFA0F);
        unicodeSet.add(0xFA11);
        unicodeSet.add(0xFA13);
        unicodeSet.add(0xFA14);
        unicodeSet.add(0xFA1F);
        unicodeSet.add(0xFA21);
        unicodeSet.add(0xFA23);
        unicodeSet.add(0xFA24);
        unicodeSet.add(0xFA27);
        unicodeSet.add(0xFA28);
        unicodeSet.add(0xFA29);

        return unicodeSet;
    }

    public static void main(String[] args) {
        System.out.println(getCodePoint("𤳳"));
        System.out.println(isCJKUnifiedIdeographicChar("𤳳"));
    }

}
