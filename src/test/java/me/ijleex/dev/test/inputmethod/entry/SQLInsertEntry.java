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

/**
 * 输入法词条 SQL格式
 *
 * <p>通过 {@link #toString()} 方法输出为 SQL 插入语句</p>
 *
 * @author ijymLee
 * @since 2018-03-21 17:14 新建
 */
public class SQLInsertEntry extends ImeEntry {

    /**
     * 输出格式
     */
    private static final String OUT_FORMAT = "INSERT INTO t_wubi_ime_wb(code,text,weight,type) VALUES(\'%s\', \'%s\', \'%s\', \'%s\');";

    /**
     * 构建输出格式为 {@value OUT_FORMAT} 的词条
     */
    public SQLInsertEntry(String code, String text, String weight, String type) {
        super(code, text, weight, type, null);
    }

    /**
     * 输出为 SQL 插入语句
     *
     * <p>格式为：{@value OUT_FORMAT}</p>
     *
     * @return 如 INSERT INTO t_wubi_ime_wb(code,text,weight,type) VALUES('lhbb', '甲子', '4096', '-#类1');
     * @since 2018-03-21 17:15:28 新建
     */
    @Override
    public String toString() {
        String code = getCode();
        String text = getText();
        String weight = getWeight();
        String type = getType();
        // String stem = getStem();
        return String.format(OUT_FORMAT, code, text, weight, type);
    }

}
