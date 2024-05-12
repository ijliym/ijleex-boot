/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.cncorpus.entry;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * 现代汉语语料库词语分词类频率表.
 *
 * @author liym
 * @since 2024-04-30 22:06 新建
 */
@Getter
@Setter
public class FrequencyExtEntry {

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

    @Override
    public String toString() {
        return this.text + '\t' + this.weight;
    }

}
