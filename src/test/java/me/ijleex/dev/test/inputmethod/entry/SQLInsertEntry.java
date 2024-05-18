/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

/**
 * 输入法词条 SQL格式
 *
 * <p>通过 {@link #toString()} 方法输出为 SQL 插入语句</p>
 *
 * @author liym
 * @since 2018-03-21 17:14 新建
 */
public class SQLInsertEntry extends ImeEntry {

    /**
     * 输出格式
     */
    private static final String OUT_FORMAT = "INSERT INTO t_ime_dict(text,code,weight,stem,type) VALUES('%s', '%s', %s, '%s', '%s');";

    /**
     * 构建输出格式为 {@value OUT_FORMAT} 的词条
     */
    public SQLInsertEntry(String text, String code, int weight, String stem, String type) {
        super(text, code, weight, stem, type);
    }

    /**
     * 输出为 SQL 插入语句
     *
     * <p>格式为：{@value OUT_FORMAT}</p>
     *
     * @return 如 INSERT INTO t_ime_dict(text,code,weight,stem,type) VALUES('一', 'a', '81694', 'av', '-');
     * @since 2018-03-21 17:15:28 新建
     */
    @Override
    public String toString() {
        String text = this.getText();
        String code = this.getCode();
        int weight = this.getWeight();
        String stem = this.getStem();
        String type = this.getType();
        return String.format(OUT_FORMAT, text, code, weight, stem, type);
    }

}
