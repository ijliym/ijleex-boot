/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.cncorpus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import me.ijleex.dev.test.inputmethod.cncorpus.entry.FrequencyExtEntry;

/**
 * 现代汉语语料库词语分词类频率表.
 *
 * @author liym
 * @since 2024-04-30 23:04:16 新建
 */
public class ExcelFrequency2Listener extends AnalysisEventListener<FrequencyExtEntry> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Integer> entryMap;
    private final List<String> humanNameList;

    public ExcelFrequency2Listener(Map<String, Integer> entryMap) {
        this(entryMap, new ArrayList<>(174));
    }

    public ExcelFrequency2Listener(Map<String, Integer> entryMap, List<String> humanNameList) {
        this.entryMap = entryMap;
        this.humanNameList = humanNameList;
    }

    @Override
    public void invoke(FrequencyExtEntry data, AnalysisContext context) {
        if ("人名".equals(data.getKind())
                || "人名-名".equals(data.getKind())
                || "数词".equals(data.getKind())) {
            this.humanNameList.add(data.getText());
            return;
        }
        // 将相同词语、不同词类名称的词频相加
        this.entryMap.merge(data.getText(), data.getWeight(), Integer::sum);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        logger.info("已加载词频：{}", this.entryMap.size());
    }

}
