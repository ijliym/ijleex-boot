/*
 * Copyright 2011-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.time;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

/**
 * Java 8 中 Date 与 LocalDateTime、LocalDate、LocalTime 互转
 *
 * @author liym
 * @see java.util.Date#from(Instant)
 * @see java.util.Date#toInstant()
 * @since 2018-11-19 11:17 新建
 */
public class Jdk8DateTimeTest {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Pattern: 2018-11-19T11:39:21.371
     *
     * @see DateTimeFormatter#ofPattern(String, java.util.Locale)
     */
    private final DateTimeFormatter formatter;
    private final SimpleDateFormat format;

    public Jdk8DateTimeTest() {
        this.formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.CHINA);
        this.format = new SimpleDateFormat(DATE_TIME_FORMAT);
    }

    /**
     * 01. java.util.Date --> java.time.LocalDateTime
     *
     * @since 2018-11-19 11:19
     */
    @Test
    public void testDateToLocalDateTime() {
        Date date = new Date();
        Instant instant = date.toInstant();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println(this.formatter.format(localDateTime));
    }

    /**
     * 02. java.util.Date --> java.time.LocalDate
     *
     * @since 2018-11-19 11:22
     */
    @Test
    public void testDateToLocalDate() {
        Date date = new Date();
        Instant instant = date.toInstant();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        System.out.println(localDate);
    }

    /**
     * 03. java.util.Date --> java.time.LocalTime
     *
     * @since 2018-11-19 11:23
     */
    @Test
    public void testDateToLocalTime() {
        Date date = new Date();
        Instant instant = date.toInstant();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalTime localTime = localDateTime.toLocalTime();
        System.out.println(localTime);
    }

    /**
     * 04. java.time.LocalDateTime --> java.util.Date
     *
     * @since 2018-11-19 11:23
     */
    @Test
    public void testLocalDateTimeToDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        Date date = Date.from(instant);
        System.out.println(this.format.format(date));
    }

    /**
     * 05. java.time.LocalDate --> java.util.Date
     *
     * @since 2018-11-19 11:24
     */
    @Test
    public void testLocalDateToDate() {
        LocalDate localDate = LocalDate.now();
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();

        Date date = Date.from(instant);
        System.out.println(this.format.format(date));
    }

    /**
     * 06. java.time.LocalTime --> java.util.Date
     *
     * @since 2018-11-19 11:27
     */
    @Test
    public void testLocalTimeToDate() {
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

        Date date = Date.from(instant);
        System.out.println(this.format.format(date));
    }

    /**
     * 07. System.currentTimeMillis() --> java.time.LocalDateTime
     *
     * @since 2018-11-19 11:34
     */
    @Test
    public void testTimeMillisToLocalDateTime() {
        long timeMillis = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(timeMillis);

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println(this.formatter.format(localDateTime));
    }

    /**
     * 08. java.time.LocalDateTime --> System.currentTimeMillis()
     *
     * @since 2021-03-30 15:50:52
     */
    @Test
    public void testLocalDateTimeToTimeMillis() {
        long timeMillis = System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.now();
        long epochMillis = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

        System.out.println(" timeMillis: " + timeMillis);
        System.out.println("epochMillis: " + epochMillis);
    }

}
