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

package me.ijleex.dev.test.inputmethod.entry;

import java.util.Objects;

/**
 * 输入法词条（五笔/郑码）
 *
 * <p>通过 {@link #toString()} 方法输出为符合 MySQL “LOAD DATA INFILE” 语法格式的字符串 </p>
 *
 * @author liym
 * @version 2018-03-20 14:25:26 实现 CharSequence，以便用 Files 中的方法读取、写入文件
 * @since 2017-08-07 15:04 新建
 */
public class ImeEntry implements CharSequence {

    /**
     * 编码
     */
    private String code;
    /**
     * 词条（即要输出的内容）
     */
    private String text;
    /**
     * 词频（权重），用于词条排序
     */
    private String weight;
    /**
     * 类型（用于多多输入法，如 #类1、#类2、#次、#用 等）
     */
    private String type;
    /**
     * 构词码（用于Rime输入法）
     */
    private String stem;

    /**
     * 构建词条
     *
     * @param code 代码，不能为空
     * @param text 词条，不能为空
     */
    public ImeEntry(String code, String text) {
        this.code = code;
        this.text = text;
    }

    /**
     * 构建词条
     *
     * @param code 代码，不能为空
     * @param text 词条，不能为空
     * @param weight 词频（权重）
     * @param type 类型
     * @param stem 构词码（用于Rime输入法）
     */
    ImeEntry(String code, String text, String weight, String type, String stem) {
        this(code, text);
        this.weight = weight;
        this.type = type;
        this.stem = stem;
    }

    public String getCode() {
        return code;
    }

    // public void setCode(String code) {
    //     this.code = code;
    // }

    public String getText() {
        return text;
    }

    // public void setText(String text) {
    //     this.text = text;
    // }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    @Override
    public int length() {
        return code.length();
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
     * 重写 equals 方法，用于判断两个词条是否相同
     *
     * @param anObject 对象
     * @return true/false
     * @see java.util.List#contains(Object)
     * @see java.util.List#indexOf(Object)
     * @see java.util.ArrayList#indexOf(java.lang.Object)
     * @since 2018-03-22 11:10:07 重写equals方法，用于 {@link me.ijleex.dev.test.inputmethod.wubi4me.Test01#testRearrange()}
     * 方法中 “entryList.indexOf(entry)” 判断
     */
    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        // 两个词条是否相同，仅判断 编码与汉字 是否相同
        if (anObject instanceof ImeEntry) {
            ImeEntry anotherEntry = (ImeEntry) anObject;
            boolean eq1 = Objects.equals(code, anotherEntry.code);
            boolean eq2 = Objects.equals(text, anotherEntry.text);
            return eq1 && eq2;
        }
        return false;
    }

    /**
     * 计算 Hash 值：{@link #code}、{@link #text}
     *
     * @return 对象的 Hash 值
     * @see java.util.HashSet#add
     * @since 2018-03-27 21:51:07 重复hashCode方法，用于 HashSet 判断重复对象
     */
    @Override
    public int hashCode() {
        // code、text 不能等于空（空字符串与NULL）
        int x = code.hashCode();
        int y = text.hashCode();
        // 乘以质数，以避免直接相加产生的偶然相等
        return x * 17 + y * 19;
    }

    /**
     * 将编码（code）与词条（text）按 TAB 分隔输出
     *
     * @return TAB 分隔的词条数据，如 “aaaa	工”、“aaaa	恭恭敬敬” 等
     */
    @Override
    public String toString() {
        return text + '\t' + code;
    }

}
