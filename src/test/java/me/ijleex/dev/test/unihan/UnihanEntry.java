/*
 * Copyright 2011-2018 ijym-lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.ijleex.dev.test.unihan;

import java.util.Objects;

/**
 * 汉字条目
 *
 * @author ijymLee
 * @version 2018-03-20 14:58:15 实现 CharSequence，因为 {@link java.nio.file.Files#write}
 * @since 2017-09-07 11:12 新建
 */
public class UnihanEntry implements CharSequence {

    private String hanChar;

    private int codePoint;
    private String codePointHex;

    private String unicodeBlock;

    /**
     * 构建
     *
     * @param hanChar 汉字
     * @since 2018-04-25 09:21
     */
    public UnihanEntry(String hanChar) {
        this.hanChar = hanChar;
    }

    public UnihanEntry(String hanChar, int codePoint, String codePointHex, String unicodeBlock) {
        this(hanChar);
        this.codePoint = codePoint;
        this.codePointHex = codePointHex;
        this.unicodeBlock = unicodeBlock;
    }

    public String getHanChar() {
        return hanChar;
    }

    public void setHanChar(String hanChar) {
        this.hanChar = hanChar;
    }

    public int getCodePoint() {
        return codePoint;
    }

    public void setCodePoint(int codePoint) {
        this.codePoint = codePoint;
    }

    public String getCodePointHex() {
        return codePointHex;
    }

    public void setCodePointHex(String codePointHex) {
        this.codePointHex = codePointHex;
    }

    public String getUnicodeBlock() {
        return unicodeBlock;
    }

    public void setUnicodeBlock(String unicodeBlock) {
        this.unicodeBlock = unicodeBlock;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException("subSequence");
    }

    /**
     * {@inheritDoc}
     *
     * @param anObject 对象
     * @return true/false
     * @since 2018-04-25 09:31 重写 equals(Object) 方法
     */
    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof UnihanEntry) {
            UnihanEntry anotherEntry = (UnihanEntry) anObject;
            boolean eq1 = Objects.equals(hanChar, anotherEntry.hanChar);
            boolean eq2 = (codePoint == anotherEntry.codePoint);
            return eq1 && eq2;
        }
        return false;
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
        int hash = hanChar.hashCode();
        return hash * 17 + codePoint * 19;
    }

    /**
     * 用来输出可以导入到 MySQL 数据库的数据格式
     *
     * @return Tab separated string
     */
    @Override
    public String toString() {
        return hanChar + "\t" + codePoint + "\t" + codePointHex + '\t' + unicodeBlock;
    }

}
