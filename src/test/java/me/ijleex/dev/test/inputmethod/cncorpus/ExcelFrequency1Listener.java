/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.cncorpus;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;
import me.ijleex.dev.test.inputmethod.cncorpus.entry.FrequencyEntry;

/**
 * 现代汉语语料库词语频率表.
 *
 * @author liym
 * @since 2024-04-30 22:21 新建
 */
public class ExcelFrequency1Listener extends AnalysisEventListener<FrequencyEntry> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Integer> entryMap;

    public ExcelFrequency1Listener(Map<String, Integer> entryMap) {
        this.entryMap = entryMap;
    }

    @Override
    public void invoke(FrequencyEntry data, AnalysisContext context) {
        this.entryMap.put(data.getText(), data.getWeight());
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        logger.info("已加载词频：{}", this.entryMap.size());
    }

}
