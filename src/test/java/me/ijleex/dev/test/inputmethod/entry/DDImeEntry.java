/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

/**
 * 输入法词条 多多格式
 *
 * <p>通过 {@link #toString()} 方法输出为与 “[新世纪五笔]/原始码表/” 中的词条相同格式的字符串。</p>
 *
 * @author liym
 * @since 2017-08-09 22:34 新建
 */
public class DDImeEntry extends ImeEntry {

    /**
     * 构建
     */
    public DDImeEntry(String code, String text, int weight, String type) {
        super(code, text, weight, type, null);
    }

    /**
     * 输出为多多输入法原始码表格式数据
     *
     * @return TAB 分隔的词条数据，如 “工	aaaa#序65535”
     * @since 2017-08-09 22:35
     */
    @Override
    public String toString() {
        String code = getCode();
        String text = getText();
        int weight = getWeight();
        // String type = getType();
        // String stem = getStem();
        return text + '\t' + code + "#序" + weight;
    }

}
