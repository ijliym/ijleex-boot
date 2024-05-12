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
public class MySQLLoadEntry extends ImeEntry {

    /**
     * 构建
     */
    public MySQLLoadEntry(String code, String text, int weight, String type, String stem) {
        super(code, text, weight, type, stem);
    }

    /**
     * 输出词条数据为可以导入到 MySQL 数据库的格式的数据
     *
     * @return TAB 分隔的词条数据，如 “aaaa	工	65535	-”、“aaaa	恭恭敬敬	15744	-#类1”等
     */
    @Override
    public String toString() {
        String code = getCode();
        String text = getText();
        int weight = getWeight();
        String type = getType();
        String stem = getStem();
        if (stem == null || stem.isEmpty()) {
            return code + '\t' + text + '\t' + weight + '\t' + type;
        } else {
            return code + '\t' + text + '\t' + weight + '\t' + type + '\t' + stem;
        }
    }

}
