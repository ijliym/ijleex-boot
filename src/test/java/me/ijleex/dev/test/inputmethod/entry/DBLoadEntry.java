/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

/**
 * 输入法词条
 *
 * <p>通过 {@link #toString()} 方法输出为符合 MySQL “LOAD DATA INFILE” 语法格式的字符串 </p>
 *
 * @author liym
 * @since 2018-04-18 14:43 新建
 */
public class DBLoadEntry extends ImeEntry {

    /**
     * 构建
     */
    public DBLoadEntry(String text, String code, int weight, String stem, String type) {
        super(text, code, weight, stem, type);
    }

    /**
     * 输出词条数据为可以导入到 MySQL 数据库的格式的数据
     *
     * @return TAB 分隔的词条数据，如 “aaaa	工	65535	-”、“aaaa	恭恭敬敬	15744	类1”等
     */
    @Override
    public String toString() {
        String text = this.getText();
        String code = this.getCode();
        int weight = this.getWeight();
        String stem = this.getStem();
        String type = this.getType();
        if (stem == null || stem.isEmpty()) {
            return text + '\t' + code + '\t' + weight + '\t' + '\t' + type;
        } else {
            return text + '\t' + code + '\t' + weight + '\t' + stem + '\t' + type;
        }
    }

}
