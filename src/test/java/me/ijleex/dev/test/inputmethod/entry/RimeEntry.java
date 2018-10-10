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
 * 输入法词条 Rime格式（http://rime.im/）
 *
 * @author ijymLee
 * @since 2018-04-18 14:18 新建
 */
public class RimeEntry extends ImeEntry {

    /**
     * 构建
     */
    public RimeEntry(String code, String text, String weight, String stem) {
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
        String weight = getWeight();
        // String type = getType();
        String stem = getStem();
        return text + '\t' + code + '\t' + weight + '\t' + stem;
    }

}
