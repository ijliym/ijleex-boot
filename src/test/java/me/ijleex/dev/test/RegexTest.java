/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * RegexTest
 *
 * @author liym
 * @since 2019-01-03 16:52 新建
 */
public final class RegexTest {

    /**
     * @since 2019-01-07 15:00 @Test for test02()
     */
    @Test
    public void test01() {
        String regex = "(?<timestamp>\\S+ \\S+) (?<pid>\\S+) \\[(?<loglevel>\\S+)] (?<message>.*)";
        Pattern pattern = Pattern.compile(regex);
        System.out.println("pattern: " + pattern.pattern());

        String str = "2019-01-03 17:12:47 15752 [Note] InnoDB: 128 rollback segment(s) are active";
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println("timestamp: " + matcher.group(1));
            System.out.println("pid: " + matcher.group(2));
            System.out.println("loglevel: " + matcher.group(3));
            System.out.println("message: " + matcher.group(4));
        }
    }

    /**
     * @since 2019-01-07 15:01
     */
    @Test
    public void test02() {
        String regex = "(?<=\\{)(.+?)(?=})";
        Pattern pattern = Pattern.compile(regex);
        System.out.println("pattern: " + pattern.pattern());

        String str = "%d{yyyy-MM-dd HH:mm:ss.SSS}";
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

}
