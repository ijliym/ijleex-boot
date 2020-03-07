/*
 * Copyright 2011-2020 ijym-lee
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
    private static final String OUT_FORMAT = "UPDATE t_ime_dict SET weight=%s,type='%s',stem='%s' WHERE code='%s' AND text='%s';";

    /**
     * 构建输出格式为 {@value OUT_FORMAT} 的词条
     *
     * @param code 代码，不能为空
     * @param text 词条，不能为空
     * @param weight 词频（权重）
     * @param type 类型
     * @param stem 构词码（用于Rime输入法）
     */
    public SQLUpdateEntry(String code, String text, String weight, String type, String stem) {
        super(code, text, weight, type, stem);
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
        String code = getCode();
        String text = getText();
        String weight = getWeight();
        String type = getType();
        String stem = getStem();
        if (isEmpty(code) || isEmpty(text)) {
            return "代码或词条为空，不能更新。";
        }
        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE t_ime_dict SET ");
        if (isNotEmpty(weight) && isInteger(weight)) {
            sql.append("weight=").append(weight);
            sql.append(", ");
        }
        if (isNotEmpty(type)) {
            sql.append("type='").append(type).append("'");
            sql.append(", ");
        }
        if (isNotEmpty(stem)) {
            sql.append("stem='").append(stem).append("'");
            sql.append(", ");
        }
        int idx = sql.lastIndexOf(", ");
        // System.out.println("idx: " + idx);
        sql = sql.replace(idx, idx + 1, "");
        if (sql.length() == 22) { // "UPDATE t_ime_dict SET " 的长度为 22
            return "没有可更新的属性：" + super.toString();
        } else {
            String whereFormat = "WHERE code='%s' AND text='%s';";
            String where = String.format(whereFormat, code, text);
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

    /**
     * @see Integer#parseInt(java.lang.String)
     */
    private static boolean isInteger(final String s) {
        if (isEmpty(s)) {
            return false;
        } else {
            char[] chars = s.toCharArray();
            for (char ch : chars) {
                if ('0' > ch || ch > '9') {
                    return false;
                }
            }
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println(new SQLUpdateEntry("a", "一", "2048", "-", "av"));
        System.out.println(new SQLUpdateEntry("a", "一", " ", "-", "av"));
        System.out.println(new SQLUpdateEntry("a", "一", "2048", "-", null));
        System.out.println(new SQLUpdateEntry("a", "成", "90６", "-", null));
    }

}
