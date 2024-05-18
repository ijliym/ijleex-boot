/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

/**
 * SQLUpdateEntry
 *
 * @author liym
 * @since 2018-05-08 16:04 新建
 */
public class SQLUpdateEntry extends ImeEntry {

    /**
     * 输出格式
     */
    private static final String OUT_FORMAT = "UPDATE t_ime_dict SET weight=%s, stem='%s', type='%s' WHERE text='%s' AND code='%s';";

    /**
     * 构建输出格式为 {@value OUT_FORMAT} 的词条
     *
     * @param text 词条，不能为空
     * @param code 代码，不能为空
     * @param weight 词频（权重）
     * @param stem 构词码（用于Rime输入法）
     * @param type 类型
     */
    public SQLUpdateEntry(String text, String code, int weight, String stem, String type) {
        super(text, code, weight, stem, type);
    }

    /**
     * 输出为 SQL 更新语句
     *
     * <p>格式为：{@value OUT_FORMAT}</p>
     *
     * @return UPDATE SET .. WHERE ..
     * @since 2018-05-08 16:06
     */
    @Override
    public String toString() {
        String text = getText();
        String code = getCode();
        int weight = getWeight();
        String stem = getStem();
        String type = getType();
        if (isEmpty(text) || isEmpty(code)) {
            return "词条或代码为空，不能更新。";
        }
        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE t_ime_dict SET ");
        if (weight > 0) {
            sql.append("weight=").append(weight);
            sql.append(", ");
        }
        if (isNotEmpty(stem)) {
            sql.append("stem='").append(stem).append("'");
            sql.append(", ");
        }
        if (isNotEmpty(type)) {
            sql.append("type='").append(type).append("'");
            sql.append(", ");
        }
        int idx = sql.lastIndexOf(", ");
        // System.out.println("idx: " + idx);
        sql.replace(idx, idx + 1, "");
        if (sql.length() == 22) { // "UPDATE t_ime_dict SET " 的长度为 22
            return "没有可更新的属性：" + super.toString();
        } else {
            String whereFormat = "WHERE text='%s' AND code='%s';";
            String where = String.format(whereFormat, text, code);
            sql.append(where);
        }
        return sql.toString();
    }

    private static boolean isEmpty(final String cs) {
        return cs == null || cs.trim().isEmpty();
    }

    private static boolean isNotEmpty(final String cs) {
        return !isEmpty(cs);
    }

    public static void main(String[] args) {
        System.out.println(new SQLUpdateEntry("一", "a", 2048, "av", "-"));
        System.out.println(new SQLUpdateEntry("一", "a", 0, "av", "-"));
        System.out.println(new SQLUpdateEntry("一", "a", 2048, null, "-"));
        System.out.println(new SQLUpdateEntry("成", "a", -1, null, "-"));
        System.out.println(new SQLUpdateEntry("一下", "aa", 3182, null, "类1"));
    }

}
