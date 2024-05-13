/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.cncorpus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    /**
     * 可以作为数词开头数字的数字
     */
    private static final char[] START_CN_DIGIT = "一二三四五六七八九十".toCharArray();
    /**
     * 数字
     */
    private static final char[] CN_DIGIT = "一二三四五六七八九十百千万０".toCharArray();

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, Integer> entryMap;
    private final List<String> ignoreList;

    public ExcelFrequency2Listener(Map<String, Integer> entryMap) {
        this(entryMap, new ArrayList<>(200));
    }

    public ExcelFrequency2Listener(Map<String, Integer> entryMap, List<String> ignoreList) {
        this.entryMap = entryMap;
        this.ignoreList = ignoreList;
    }

    @Override
    public void invoke(FrequencyExtEntry data, AnalysisContext context) {
        if ("人名".equals(data.getKind())
                || ("人名-名".equals(data.getKind()) && data.length() > 1)) {
            this.ignoreList.add(data.getText());
            return;
        }

        if ("数词".equals(data.getKind()) && this.shouldIgnoreNumeric(data.getText())) {
            this.ignoreList.add(data.getText());
            return;
        }

        // 将相同词语、不同词类名称的词频相加
        this.entryMap.merge(data.getText(), data.getWeight(), Integer::sum);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        logger.info("已加载词频：{}", this.entryMap.size());
    }

    /**
     * 移除全为数字组成的”数词“，如”二十三“”二十一“”一九七九“，保留”第六“”几个“这类”“数词
     *
     * @param text 数词
     * @return 是否移除
     * @since 2024-05-13 22:15
     */
    private boolean shouldIgnoreNumeric(String text) {
        // 保留 千百、一、两、三、…… 等词语
        if ("千百".equals(text) || text.length() == 1) {
            return false;
        }

        char[] textChars = text.toCharArray();
        char firstCh = textChars[0];
        int idx = Arrays.binarySearch(START_CN_DIGIT, firstCh);
        if (idx < 0) {
            return false;
        }

        boolean ignore = true;
        for (char ch : textChars) {
            idx = Arrays.binarySearch(CN_DIGIT, ch);
            if (idx < 0) {
                ignore = false;
                break;
            }
        }
        return ignore;
    }

    public static void main(String[] args) {
        ExcelFrequency2Listener listener = new ExcelFrequency2Listener(new HashMap<>());
        System.out.println(listener.shouldIgnoreNumeric("二十八"));  // true
        System.out.println(listener.shouldIgnoreNumeric("第六"));   // false
        System.out.println(listener.shouldIgnoreNumeric("八百"));   // true
        System.out.println(listener.shouldIgnoreNumeric("几万"));   // false
        System.out.println(listener.shouldIgnoreNumeric("第六"));   // false
        System.out.println(listener.shouldIgnoreNumeric("一九八０"));   // true
    }

    static {
        Arrays.sort(START_CN_DIGIT);
        Arrays.sort(CN_DIGIT);
    }
}
