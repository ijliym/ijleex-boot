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
     * 符合<a href="https://dev.mysql.com/doc/refman/8.3/en/load-data.html">MySQL “LOAD DATA INFILE”</a>语法格式的字符串
     */
    MySQL,

    /**
     * <a href="https://chinput.com/portal.php">多多输入法</a>码表格式
     *
     * <p>如{@code 一	a#序81119}，TAB分隔</p>
     */
    DuoDuo,

    /**
     * <a href="https://rime.im/">Rime输入法</a>格式
     * <p>
     * 如{@code 一	a	81119}
     * </p>
     * Rime 的词条格式不固定，因为可以在定义码表时，定义格式。
     * 可用的参数有 code、text、weight、stem。
     */
    Rime,

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
