/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.system;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

/**
 * 系统 “环境变量”
 *
 * @author liym
 * @since 2018-09-26 11:20 新建
 */
public class SystemPropsTest {

    /**
     * 打印系统环境变量
     *
     * @since 2017-08-09 10:09
     */
    @Test
    public void printSysEnv() {
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
    public void printSysProps() {
        Properties props = System.getProperties();
        Set<Map.Entry<Object, Object>> entrySet = props.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + "\t\t" + value);
        }
    }

}
