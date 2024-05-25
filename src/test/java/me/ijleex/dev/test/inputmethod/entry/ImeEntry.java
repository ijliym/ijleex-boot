/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * 输入法词条（五笔/郑码）
 *
 * <p>通过 {@link #toString()} 方法输出为符合 MySQL “LOAD DATA INFILE” 语法格式的字符串 </p>
 *
 * @author liym
 * @version 2018-03-20 14:25:26 实现 CharSequence，以便用 Files 中的方法读取、写入文件
 * @see java.util.SortedSet
 * @see java.util.TreeSet#add(java.lang.Object)
 * @see #compareTo(ImeEntry)
 * @since 2017-08-07 15:04 新建
 * @since 2024-05-09 23:10 实现 Comparable
 */
@Getter
public class ImeEntry implements CharSequence, Comparable<ImeEntry> {

    /**
     * 词条（即要输出的内容）
     */
    private final String text;
    /**
     * 编码
     */
    private final String code;

    /**
     * 词频（权重），用于词条排序
     */
    @Setter
    private int weight;
    /**
     * 构词码（用于Rime输入法）
     */
    private final String stem;

    /**
     * 类型（用于多多输入法，如{@code -}、{@code 类1}、{@code 类2}、{@code 类3}、…、{@code 次}、{@code 用}等）
     */
    private final String type;

    /**
     * 构建词条
     *
     * @param text 词条，不能为空
     * @param code 代码，不能为空
     */
    public ImeEntry(String text, String code) {
        this(text, code, 0);
    }

    /**
     * 构建词条
     *
     * @param text 词条，不能为空
     * @param code 代码，不能为空
     * @param weight 词频
     * @since 2024-05-09 22:44
     */
    public ImeEntry(String text, String code, int weight) {
        this(text, code, weight, null, null);
    }

    /**
     * 构建词条
     *
     * @param text 词条，不能为空
     * @param code 代码，不能为空
     * @param weight 词频（权重）
     * @param stem 构词码（用于Rime输入法）
     * @param type 类型
     */
    protected ImeEntry(String text, String code, int weight, String stem, String type) {
        this.text = text;
        this.code = code;
        this.weight = weight;
        this.stem = stem;
        this.type = type;
    }

    @Override
    public int length() {
        return this.code.length();
    }

    @Override
    public char charAt(int index) {
        throw new UnsupportedOperationException("charAt");
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("subSequence");
    }

    /**
     * 判断当前词条是否为词组
     *
     * @return true/false
     * @since 2024-05-25 16:26
     */
    private boolean isPhrase() {
        return "类1".equals(this.type);
    }

    /**
     * 重写 equals 方法，用于判断两个词条是否相同
     *
     * @param obj 对象
     * @return true/false
     * @see java.util.List#contains(Object)
     * @see java.util.List#indexOf(Object)
     * @see java.util.ArrayList#indexOf(java.lang.Object)
     * @since 2018-03-22 11:10:07 重写equals方法
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        // 两个词条是否相同，仅判断 编码与汉字 是否相同
        ImeEntry that = (ImeEntry) obj;
        return Objects.equals(this.text, that.text) && Objects.equals(this.code, that.code);
    }

    /**
     * 计算 Hash 值：{@link #text}、{@link #code}
     *
     * @return 对象的 Hash 值
     * @see java.util.HashSet#add
     * @since 2018-03-27 21:51:07 重复hashCode方法，用于 HashSet 判断重复对象
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.text, this.code);
    }

    /**
     * 词条排序（基于编码、词频）.
     *
     * <p>词条排序：先根据编码（字母）升序排序，再根据词频降序排序。</p>
     *
     * @param that 待对比对象
     * @return 顺序
     * @see java.util.TreeMap#put(Object, Object)
     * @since 2024-05-09 22:36
     */
    @Override
    public int compareTo(ImeEntry that) {
        if (that == null) {
            return 1;
        }

        // 先按编码升序排序
        String thisCode = this.getCode();
        String thatCode = that.getCode();
        boolean isPhrase = this.isPhrase();
        int result = 0;
        if (isPhrase) { // 使词组的二字词排在前面
            int thisLen = thisCode.length();
            int thatLen = thatCode.length();
            result = Integer.compare(thisLen, thatLen);
        }
        if (result == 0) {
            result = thisCode.compareTo(thatCode);
            if (result == 0) {
                // 再按词频降序排序
                int thisWeight = this.getWeight();
                int thatWeight = that.getWeight();
                result = Integer.compare(thatWeight, thisWeight);
                if (result == 0) {
                    // 最后按词条的Unicode码升序排序 2018-08-24 17:57
                    String thisText = this.getText();
                    String thatText = that.getText();
                    result = thisText.compareTo(thatText);
                }
            }
        }
        return result;
    }

    /**
     * 将词条（text）与编码（code）按 TAB 分隔输出
     *
     * <p>默认输出符合<a href="https://rime.im/">RIME</a>输入法格式的词条，格式如下：</p>
     *
     * <pre>
     * columns:
     *   - text
     *   - code
     *   - weight
     *   - stem
     * </pre>
     *
     * @return TAB 分隔的词条数据，如 “一	a	1	av”、“一下	aa	3182” 等
     */
    @Override
    public String toString() {
        if (this.stem == null || this.stem.isEmpty()) {
            return this.text + '\t' + this.code + '\t' + this.weight;
        } else {
            return this.text + '\t' + this.code + '\t' + this.weight + '\t' + this.stem;
        }
    }

}
