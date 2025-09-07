/*
 * Copyright 2011-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

/**
 * <a href="https://rime.im/">RIME输入法</a>格式词条
 *
 * @author liym
 * @since 2018-04-18 14:18 新建
 */
public class RimeEntry extends ImeEntry {

    /**
     * 构建
     */
    public RimeEntry(String text, String code, int weight, String stem, String type) {
        super(text, code, weight, stem, type);
    }

}
