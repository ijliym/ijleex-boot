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

package me.ijleex.dev.test.time;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

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
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT, Locale.CHINA);

    private SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);

    /**
     * 01. java.util.Date --> java.time.LocalDateTime
     *
     * @since 2018-11-19 11:19
     */
    @Test
    public void testDateToLocalDateTime() {
        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        System.out.println(formatter.format(localDateTime));
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
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

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
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);

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
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();

        Date date = Date.from(instant);
        System.out.println(format.format(date));
    }

    /**
     * 05. java.time.LocalDate --> java.util.Date
     *
     * @since 2018-11-19 11:24
     */
    @Test
    public void testLocalDateToDate() {
        LocalDate localDate = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();

        Date date = Date.from(instant);
        System.out.println(format.format(date));
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

        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();

        Date date = Date.from(instant);
        System.out.println(format.format(date));
    }

    /**
     * 07. System.currentTimeMillis() --> java.time.LocalDateTime
     *
     * @since 2018-11-19 11:34
     */
    @Test
    public void testCurrentTimeMillisToLocalTime() {
        long timeMillis = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(timeMillis);
        ZoneId zone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        System.out.println(formatter.format(localDateTime));
    }

}
