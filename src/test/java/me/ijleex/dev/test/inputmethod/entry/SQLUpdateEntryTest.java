/*
 * Copyright © 2011-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.entry;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLUpdateEntry Test.
 *
 * @author liym
 * @since 2026-10-09 15:49 新建
 */
class SQLUpdateEntryTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SQLUpdateEntryTest() {
    }

    @Test
    void testToString() {
        logger.info("{}", new SQLUpdateEntry("一", "a", 2048, "av", "-"));
        logger.info("{}", new SQLUpdateEntry("一", "a", 0, "av", "-"));
        logger.info("{}", new SQLUpdateEntry("一", "a", 2048, null, "-"));
        logger.info("{}", new SQLUpdateEntry("成", "a", -1, null, "-"));
        logger.info("{}", new SQLUpdateEntry("一下", "aa", 3182, null, "类1"));
    }

}
