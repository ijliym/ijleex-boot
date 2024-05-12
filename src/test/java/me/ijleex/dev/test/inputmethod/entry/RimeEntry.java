/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

/**
 * <a href="https://rime.im/">Rime输入法</a>格式词条
 *
 * @author liym
 * @since 2018-04-18 14:18 新建
 */
public class RimeEntry extends ImeEntry {

    /**
     * 构建
     */
    public RimeEntry(String code, String text, int weight, String stem) {
        super(code, text, weight, null, stem);
    }

    /**
     * 输出为符合 Rime dictionary 定义的如下格式的数据：
     *
     * <pre>
     * columns:
     *   - text
     *   - code
     *   - weight
     *   - stem
     * </pre>
     *
     * @return TAB 分隔的词条数据，如 “一	a	1	av”
     */
    @Override
    public String toString() {
        String code = getCode();
        String text = getText();
        int weight = getWeight();
        // String type = getType();
        String stem = getStem();
        return text + '\t' + code + '\t' + weight + '\t' + stem;
    }

}
