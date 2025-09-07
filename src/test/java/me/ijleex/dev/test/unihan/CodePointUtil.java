/*
 * Copyright 2011-2025 the original author or authors.
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
     * 中日韩统一表意文字集合
     */
    private static final UnicodeSet CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET = new UnicodeSet();

    static {
        buildCJKUnifiedIdeographsSet();
    }

    private CodePointUtil() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * 计算字符的 Unicode 编码值
     *
     * @param ch 字符
     * @return 字符的 Unicode 编码，十进制
     * @since 2017-09-07 10:45
     */
    // com.ibm.icu.text.UnicodeSet#getSingleCP
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
        return CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.contains(ch);
    }

    /**
     * 判断一个字符是否属于<b>中日韩统一表意文字</b>
     *
     * @param codePoint 字符代码点
     * @return 字符是 CJK 统一汉字，返回 true，否则，返回 false
     * @since 2025-09-07 15:57
     */
    public static boolean isCJKUnifiedIdeographicChar(int codePoint) {
        return CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.contains(codePoint);
    }

    /**
     * 获取中日韩统一表意文字集合
     * <a href="https://en.wikipedia.org/wiki/Template:CJK_ideographs_in_Unicode">CJK ideographs in Unicode</a>
     *
     * @return {@link UnicodeSet}
     * @since 2025-09-07 13:46
     */
    public static UnicodeSet getCJKUnifiedIdeographsSet() {
        return CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET;
    }

    /**
     * 构造中日韩统一表意文字集合
     *
     * @see <a href="https://en.wikipedia.org/wiki/Han_unification#Unicode_ranges">Unicode ranges</a>
     * @since 2019-08-25 20:54
     */
    private static void buildCJKUnifiedIdeographsSet() {
        if (!CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.isEmpty()) {
            return;
        }
        // CJK Unified Ideographs (中日韩统一表意文字)  U+4E00..U+9FFF
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x4E00, 0x9FFF); // 20,992

        // CJK Unified Ideographs Extension A (中日韩统一表意文字扩展A)  U+3400..U+4DBF
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x3400, 0x4DBF); // 6,592

        // CJK Unified Ideographs Extension B  U+20000..U+2A6DF
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x20000, 0x2A6DF); // 42,720

        // CJK Unified Ideographs Extension C  U+2A700..U+2B73F
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x2A700, 0x2B739); // 4,154

        // CJK Unified Ideographs Extension D  U+2B740..U+2B81F
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x2B740, 0x2B81D); // 222

        // CJK Unified Ideographs Extension E  U+2B820..U+2CEAF
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x2B820, 0x2CEA1); // 5,762

        // CJK Unified Ideographs Extension F  U+2CEB0..U+2EBEF
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x2CEB0, 0x2EBE0); // 7,473

        // CJK Unified Ideographs Extension G  U+30000..U+3134F
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x30000, 0x3134A); // 4,939

        // CJK Unified Ideographs Extension H  U+31350..U+323AF
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x31350, 0x323AF); // 4,192

        // CJK Unified Ideographs Extension I  U+2EBF0..U+2EE5F
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0x2EBF0, 0x2EE5D); // 4,192

        // CJK Compatibility Ideographs (中日韩兼容表意文字)  U+F900..U+FAFF
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA0E); // 12 Unified
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA0F);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA11);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA13);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA14);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA1F);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA21);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA23);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA24);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA27);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA28);
        CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.add(0xFA29);

        // int size = CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.size(); // 97,680
        // System.out.println("中日韩统一表意文字 字数：" + size);
    }

    /**
     * @param str
     * @return
     * @since 2025-09-07 17:11
     */
    // com.ibm.icu.text.UnicodeSet#getSingleCP
    public static boolean isPhrase(String str) {
        int codePoint = getSingleCP(str);
        return codePoint == -1;
    }

    /**
     * Utility for getting code point from single code point CharSequence.
     * See the public UTF16.getSingleCodePoint() (which returns -1 for null rather than throwing NPE).
     *
     * @param s to test
     * @return a code point IF the string consists of a single one.
     * otherwise returns -1.
     */
    private static int getSingleCP(CharSequence s) {
        if (s.length() == 1) return s.charAt(0);
        if (s.length() == 2) {
            int cp = Character.codePointAt(s, 0);
            if (cp > 0xFFFF) { // is surrogate pair
                return cp;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(getCodePoint("𤳳"));
        System.out.println(isCJKUnifiedIdeographicChar("𤳳"));
        System.out.println(CJK_UNIFIED_IDEOGRAPHS_UNICODE_SET.contains("\uD853\uDC38")); // 𤰸
        System.out.println(isPhrase("六十四"));
        System.out.println(isPhrase("六十"));
        System.out.println(isPhrase("六"));
    }

}
