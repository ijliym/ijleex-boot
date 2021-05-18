/*
 * Copyright 2011-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RegexTest
 *
 * @author liym
 * @since 2019-01-03 16:52 新建
 */
public final class RegexTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @since 2019-01-07 15:00 @Test for test02()
     */
    @Test
    public void test01() {
        String regex = "(?<timestamp>\\S+ \\S+) (?<pid>\\S+) \\[(?<loglevel>\\S+)] (?<message>.*)";
        Pattern pattern = Pattern.compile(regex);
        logger.info("Regex: {}", pattern.pattern());

        String str = "2019-01-03 17:12:47 15752 [Note] InnoDB: 128 rollback segment(s) are active";
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            logger.debug("timestamp: {}", matcher.group(1));
            logger.debug("pid: {}", matcher.group(2));
            logger.debug("loglevel: {}", matcher.group(3));
            logger.debug("message: {}", matcher.group(4));
        }
    }

    /**
     * @since 2019-01-07 15:01
     */
    @Test
    public void test02() {
        String regex = "(?<=\\{)(.+?)(?=})";
        Pattern pattern = Pattern.compile(regex);
        logger.info("Regex: {}", pattern.pattern());

        String str = "%d{yyyy-MM-dd HH:mm:ss.SSS}";
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            logger.debug(matcher.group(1));
        }
    }

    /**
     * 正则匹配分卷压缩文件名 .z01、.z02、……
     *
     * @since 2021-02-25 17:29:21
     */
    @Test
    public void test03() {
        String regex = "(\\S*)\\.(z\\d*)$"; // "(?<simpleName>\\S*)\\.(?<ext>z\\d*)$"
        Pattern pattern = Pattern.compile(regex);
        logger.info("Regex: {}", pattern.pattern());

        String name = "tomcat.z01";
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            logger.debug("name: {}", matcher.group(0));
            logger.debug("simpleName: {}", matcher.group(1));
            logger.debug("ext: {}", matcher.group(2));
        }
    }

    /**
     * 正则匹配手机号码
     *
     * @since 2021-05-18 09:13:53
     */
    @Test
    public void test04() {
        String regex = "(?<!\\d)(?<!\\.)1([38]\\d|4[01456879]|5[0-35-9]|6[2567]|7[0-8]|8[0-9]|9[0-35-9])\\d{8}(?!\\d)";
        Pattern pattern = Pattern.compile(regex);
        logger.info("Regex: {}", pattern.pattern());

        String mobileNo = "15812345678";
        Matcher matcher = pattern.matcher(mobileNo);
        boolean matches = matcher.matches();
        logger.debug("matches: {}", matches);
    }

}
