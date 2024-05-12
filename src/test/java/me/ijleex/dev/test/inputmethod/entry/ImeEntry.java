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
     * 编码
     */
    private final String code;
    /**
     * 词条（即要输出的内容）
     */
    private final String text;
    /**
     * 词频（权重），用于词条排序
     */
    @Setter
    private int weight;

    /**
     * 类型（用于多多输入法，如 #类1、#类2、#次、#用 等）
     */
    private final String type;
    /**
     * 构词码（用于Rime输入法）
     */
    private final String stem;

    /**
     * 构建词条
     *
     * @param code 代码，不能为空
     * @param text 词条，不能为空
     */
    public ImeEntry(String code, String text) {
        this(code, text, 0);
    }

    /**
     * 构建词条
     *
     * @param code 代码，不能为空
     * @param text 词条，不能为空
     * @param weight 词频
     * @since 2024-05-09 22:44
     */
    public ImeEntry(String code, String text, int weight) {
        this(code, text, weight, null, null);
    }

    /**
     * 构建词条
     *
     * @param code 代码，不能为空
     * @param text 词条，不能为空
     * @param weight 词频（权重）
     * @param type 类型
     * @param stem 构词码（用于Rime输入法）
     */
    ImeEntry(String code, String text, int weight, String type, String stem) {
        this.code = code;
        this.text = text;
        this.weight = weight;
        this.type = type;
        this.stem = stem;
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
     * 重写 equals 方法，用于判断两个词条是否相同
     *
     * @param obj 对象
     * @return true/false
     * @see java.util.List#contains(Object)
     * @see java.util.List#indexOf(Object)
     * @see java.util.ArrayList#indexOf(java.lang.Object)
     * @since 2018-03-22 11:10:07 重写equals方法，用于 {@link me.ijleex.dev.test.inputmethod.wubi4me.WubiTest#testRearrange()}
     * 方法中 “entryList.indexOf(entry)” 判断
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
        return Objects.equals(this.code, that.code) && Objects.equals(this.text, that.text);
    }

    /**
     * 计算 Hash 值：{@link #code}、{@link #text}
     *
     * @return 对象的 Hash 值
     * @see java.util.HashSet#add
     * @since 2018-03-27 21:51:07 重复hashCode方法，用于 HashSet 判断重复对象
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.code, this.text);
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
        int result = thisCode.compareTo(thatCode);
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
        return result;
    }

    /**
     * 将编码（code）与词条（text）按 TAB 分隔输出
     *
     * @return TAB 分隔的词条数据，如 “aaaa	工”、“aaaa	恭恭敬敬” 等
     */
    @Override
    public String toString() {
        return this.text + '\t' + this.code;
    }

}
