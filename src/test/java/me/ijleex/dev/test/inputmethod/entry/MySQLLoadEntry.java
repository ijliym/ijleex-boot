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
 * 输入法词条
 *
 * <p>通过 {@link #toString()} 方法输出为符合 MySQL “LOAD DATA INFILE” 语法格式的字符串 </p>
 *
 * @author ijymLee
 * @since 2018-04-18 14:43 新建
 */
public class MySQLLoadEntry extends ImeEntry {

    /**
     * 构建
     */
    public MySQLLoadEntry(String code, String text, String weight, String type, String stem) {
        super(code, text, weight, type, stem);
    }

    /**
     * 输出词条数据为可以导入到 MySQL 数据库的格式的数据
     *
     * @return TAB 分隔的词条数据，如 “aaaa	工	65535	-”、“aaaa	恭恭敬敬	15744	-#类1”等
     */
    @Override
    public String toString() {
        String code = getCode();
        String text = getText();
        String weight = getWeight();
        String type = getType();
        String stem = getStem();
        if (stem == null || stem.isEmpty()) {
            return code + '\t' + text + '\t' + weight + '\t' + type;
        } else {
            return code + '\t' + text + '\t' + weight + '\t' + type + '\t' + stem;
        }
    }

}
