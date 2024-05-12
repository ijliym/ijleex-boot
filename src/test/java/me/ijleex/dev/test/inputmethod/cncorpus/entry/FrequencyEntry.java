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
 * 现代汉语语料库词语频率表.
 *
 * @author liym
 * @since 2024-04-30 22:06 新建
 */
@Getter
@Setter
public class FrequencyEntry {

    /**
     * 词语
     */
    @ExcelProperty(index = 1)
    private String text;
    /**
     * 出现次数
     */
    @ExcelProperty(index = 2)
    private Integer weight;

    public FrequencyEntry() {
    }

    @Override
    public String toString() {
        return this.text + '\t' + this.weight;
    }

}
