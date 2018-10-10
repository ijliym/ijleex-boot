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

package me.ijleex.dev.test.system;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * 系统 “环境变量”
 *
 * @author liym
 * @since 2018-09-26 11:20 新建
 */
class SystemPropsTest {

    /**
     * 打印系统环境变量
     *
     * @since 2017-08-09 10:09
     */
    @Test
    void printSysEnv() {
        Map<String, String> vars = System.getenv();
        Set<Map.Entry<String, String>> entrySet = vars.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key + "\t\t" + value);
        }
    }

    /**
     * 打印系统属性
     *
     * @since 2018-03-08 10:05
     */
    @Test
    void printSysProps() {
        Properties props = System.getProperties();
        Set<Map.Entry<Object, Object>> entrySet = props.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "\t\t" + value);
        }
    }

}
