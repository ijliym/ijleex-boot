/*
 * Copyright © 2011-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.unihan;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.UnicodeSet;

/**
 * 简单测试
 *
 * @author liym
 * @since 2017-09-07 09:12 新建
 */
final class Icu4jTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Icu4jTest() {
    }

    @Test
    public void icu4jTest() {
        UnicodeSet unicodeSet = new UnicodeSet();
        unicodeSet.add(0xFA0E);
        unicodeSet.add(0xFA0F);
        unicodeSet.add(0xFA11);
        unicodeSet.add(0xFA13);
        unicodeSet.add(0xFA14);
        unicodeSet.add(0xFA1F);
        unicodeSet.add(0xFA21);
        unicodeSet.add(0xFA23);
        unicodeSet.add(0xFA24);
        unicodeSet.add(0xFA27);
        unicodeSet.add(0xFA28);
        unicodeSet.add(0xFA29);

        int size = unicodeSet.size();
        logger.info("size: {}", size);

        unicodeSet.forEach(logger::info);

        int codePoint = CodePointUtil.getCodePoint("\uD869\uDC1A");
        logger.info("codePoint: {}", codePoint);
        logger.info("codePointAsHex: {}", CodePointUtil.intAsHex(codePoint));
    }

}
