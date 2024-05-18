/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod;

/**
 * 处理的词条输出的格式
 *
 * @author liym
 * @since 2018-04-18 14:05 新建
 */
public enum FormatType {

    /**
     * <a href="https://rime.im/">Rime输入法</a>格式
     *
     * <p>
     * 使用<b>Tab</b>分隔的词条列数据格式，各列分别表示：text【文本】、code【编码】、weight【权重】、stem【造词码】。
     * </p>
     * <p>如{@code 一	a	81694	av}</p>
     */
    RIME,

    /**
     * 符合<a href="https://dev.mysql.com/doc/refman/8.3/en/load-data.html">MySQL “LOAD DATA INFILE”</a>语法格式的字符串
     */
    DB_DATA_LOAD,

    /**
     * SQL 插入格式
     */
    SQL_INSERT,

    /**
     * SQL 更新格式
     *
     * @since 2018-05-28 10:25
     */
    SQL_UPDATE
}
