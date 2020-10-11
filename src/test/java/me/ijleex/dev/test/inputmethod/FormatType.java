/*
 * Copyright 2011-2020 the original author or authors.
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
     * 符合 MySQL “LOAD DATA INFILE” 语法格式的字符串
     *
     * <p>https://dev.mysql.com/doc/refman/8.0/en/load-data.html</p>
     */
    MySQL,

    /**
     * 多多输入法码表格式，如 “工	aaaa#序65535”，TAB 分隔
     *
     * <p>https://chinput.com/portal.php</p>
     */
    DuoDuo,

    /**
     * Rime 输入法格式，如 一	a	av；Rime 的词条格式不固定，因为可以在定义码表时，定义格式。
     * 可用的参数有 code、text、weight、stem。
     *
     * <p>https://rime.im/</p>
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
    SQL_UPDATE;

}
