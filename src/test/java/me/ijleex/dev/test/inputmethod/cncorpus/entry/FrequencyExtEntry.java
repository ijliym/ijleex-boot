/*
 * Copyright 2011-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.cncorpus.entry;

import cn.idev.excel.annotation.ExcelProperty;

/**
 * 现代汉语语料库词语分词类频率表.
 *
 * @author liym
 * @since 2024-04-30 22:06 新建
 */
public class FrequencyExtEntry implements CharSequence {

    /**
     * 词语
     */
    @ExcelProperty(index = 1)
    private String text;
    /**
     * 词类名称
     */
    @ExcelProperty(index = 3)
    private String kind;
    /**
     * 出现次数
     */
    @ExcelProperty(index = 4)
    private Integer weight;

    public FrequencyExtEntry() {
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getKind() {
        return this.kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public int length() {
        return this.text.length();
    }

    @Override
    public char charAt(int index) {
        return this.text.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("subSequence");
    }

    @Override
    public String toString() {
        return this.text + '\t' + this.weight;
    }

}
