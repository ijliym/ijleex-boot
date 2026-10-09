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

/**
 * CodePointUtil Test.
 *
 * @author liym
 * @since 2026-10-09 16:18 新建
 */
class CodePointUtilTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private CodePointUtilTest() {
    }

    @Test
    void getCodePoint() {
        logger.info("𤳳: {}", CodePointUtil.getCodePoint("𤳳"));
    }

    @Test
    void isCJKUnifiedIdeographicChar() {
        logger.info("𤳳: {}", CodePointUtil.isCJKUnifiedIdeographicChar("𤳳"));
        logger.info("𤰸: {}", CodePointUtil.isCJKUnifiedIdeographicChar("\uD853\uDC38")); // 𤰸
    }

    @Test
    void isPhrase() {
        logger.info("六十四: {}", CodePointUtil.isPhrase("六十四"));
        logger.info("六十: {}", CodePointUtil.isPhrase("六十"));
        logger.info("六: {}", CodePointUtil.isPhrase("六"));
    }

}
