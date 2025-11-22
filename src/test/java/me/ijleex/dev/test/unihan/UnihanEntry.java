/*
 * Copyright 2011-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.unihan;

import java.util.Objects;

/**
 * 汉字条目
 *
 * @author liym
 * @version 2018-03-20 14:58:15 实现 CharSequence，因为 {@link java.nio.file.Files#write}
 * @since 2017-09-07 11:12 新建
 */
public record UnihanEntry(String hanChar, int codePoint, String codePointHex, String unicodeBlock)
        implements CharSequence {

    /**
     * 构建
     *
     * @param hanChar 汉字
     * @param codePoint 代码点，即 Unicode 代码值
     * @param codePointHex 代码点十六进制值
     * @param unicodeBlock 所属的 Unicode 分块
     * @since 2019-09-18 13:36
     */
    public UnihanEntry {
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        throw new UnsupportedOperationException("charAt");
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("subSequence");
    }

    /**
     * {@inheritDoc}
     *
     * @param o 对象
     * @return true/false
     * @since 2018-04-25 09:31 重写 equals(Object) 方法
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UnihanEntry that)) {
            return false;
        }
        return Objects.equals(this.hanChar, that.hanChar) && this.codePoint == that.codePoint;
    }

    /**
     * {@inheritDoc}
     *
     * @return Hash code
     * @since 2018-04-25 09:25:30 重写 hashCode() 方法
     */
    @Override
    public int hashCode() {
        // hanChar 不能等于空（空字符串与NULL）
        return Objects.hash(this.hanChar, this.codePoint);
    }

    /**
     * 用来输出可以导入到 MySQL 数据库的数据格式
     *
     * @return Tab separated string
     */
    @Override
    public String toString() {
        return this.hanChar + "\t" + this.codePoint + "\t" + this.codePointHex + "\t" + this.unicodeBlock;
    }

}
