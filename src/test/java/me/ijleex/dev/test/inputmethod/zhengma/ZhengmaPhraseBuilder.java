/*
 * Copyright 2011-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.zhengma;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ijleex.dev.test.inputmethod.FormatType;
import me.ijleex.dev.test.inputmethod.ImeDictAnalyzer;
import me.ijleex.dev.test.inputmethod.Utils;
import me.ijleex.dev.test.inputmethod.cncorpus.ExcelFrequencyLoad;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;

/**
 * 郑码构词 工具类
 *
 * <p>
 * 郑码词组规则：
 * <li>两字词：取第一个字的首根和次根各 1 码，取第二个字的首根和次根各 1 码（2+2）</li>
 * <li>三字词：取第一字首根 1 码，取第二字首根和次根各 1 码，取第三字首根 1 码（1+2+1）</li>
 * <li>四字及四字以上的词组：取第一字首根 1 码，取第二字首根 1 码，取第三字首根 1 码，取第四字首根 1 码（1+1+1+1）</li>
 * </p>
 *
 * @author liym
 * @see #loadStemFile(String, String)
 * @see #build(Map, Collection)
 * @see ZhengmaPhraseBuilderTest
 * @since 2018-06-06 09:13:20 新建
 * @since 2024-05-08 23:00 重构为工具类
 */
public final class ZhengmaPhraseBuilder {

    /**
     * 输出格式
     *
     * @since 2018-03-21 15:54:38
     */
    private static final FormatType OUT_FORMAT = FormatType.RIME;

    private static final Logger logger = LoggerFactory.getLogger(ZhengmaPhraseBuilder.class);

    private ZhengmaPhraseBuilder() {
        throw new IllegalAccessError("No instance");
    }

    /**
     * 加载构词码
     *
     * @return 构词码
     * @since 2024-05-14 22:45
     */
    public static Map<String, String> loadStemMap() {
        String stemFile = "InputMethod/[超集郑码]/原始码表/构词码.txt";
        return loadStemFile(getDocPath(), stemFile);
    }

    /**
     * 加载构词码
     *
     * @param startPath 文件位置
     * @param stemFile 文件路径
     * @return 构词码
     */
    private static Map<String, String> loadStemFile(String startPath, String stemFile) {
        Path srcPath = Paths.get(startPath, stemFile);
        List<String> lines;
        try {
            lines = Files.readAllLines(srcPath);
        } catch (IOException e) {
            logger.error("loadStemFile error");
            return Collections.emptyMap();
        }
        logger.info("构词码文件行数：{}", lines.size());

        Map<String, String> stemMap = new HashMap<>(lines.size());
        for (String line : lines) {
            String[] linePair = Utils.tokenizeToStringArray(line, "\t"); // 一	av
            String text = linePair[0]; // 汉字
            String stem = linePair[1]; // 构词码
            stemMap.put(text, stem);
        }
        logger.info("已加载构词码：{}", stemMap.size());
        return stemMap;
    }

    /**
     * 构建郑码词组
     *
     * @param stemMap 构词码
     * @param srcPhrasesList 构词源
     * @return 输出构词
     * @since 2018-06-06 09:18:14
     * @since 2024-05-08 23:29 重构为工具方法
     */
    public static SortedSet<ImeEntry> build(Map<String, String> stemMap, Collection<String> srcPhrasesList) {
        // 词频
        final Map<String, Integer> weightMap = new HashMap<>(15000);
        SortedSet<ImeEntry> resultSet = new TreeSet<>();
        for (String text : srcPhrasesList) {
            int length = text.length();
            if (length == 2) { // 二字词
                build2CharsCode(text, stemMap, weightMap, resultSet, OUT_FORMAT);
            } else if (length == 3) { // 三字词
                build3CharsCode(text, stemMap, weightMap, resultSet, OUT_FORMAT);
            } else if (length >= 4) { // 四字词及四字以上的词组
                build4CharsCode(text, stemMap, weightMap, resultSet, OUT_FORMAT);
            } else {
                logger.warn("不支持构词：{}", text);
            }
        }
        return resultSet;
    }

    /**
     * 二字词
     *
     * <p>构词规则：每个字各打一、二两码（2+2）</p>
     *
     * @param text 词组
     * @param stemMap 构词码
     * @param weightMap 词频
     * @param resultSet 输出构词
     * @param formatType 输出格式
     * @since 2018-03-21 13:39:49
     */
    private static void build2CharsCode(String text, Map<String, String> stemMap, Map<String, Integer> weightMap,
            SortedSet<ImeEntry> resultSet, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String stem1 = getStem(stemMap, ch1);
        String stem2 = getStem(stemMap, ch2);
        String code1 = stem1.substring(0, 2);
        String code2 = stem2.substring(0, 2);

        String code = code1 + code2;
        int weight = getWeight(weightMap, text);

        ImeDictAnalyzer.addEntryToList(resultSet, text, code, weight, null, "类1", formatType);
    }

    /**
     * 根据汉字获取代码（构词码）
     *
     * @param stemMap 构词码
     * @param ch 汉字
     * @return 代码
     * @since 2018-03-21 17:39:50
     */
    private static String getStem(Map<String, String> stemMap, String ch) {
        return stemMap.get(ch);
    }

    /**
     * 获取词频
     *
     * @param weightMap 词频集合
     * @param ch 词语
     * @return 词频
     * @since 2024-05-09 00:23
     */
    private static int getWeight(Map<String, Integer> weightMap, String ch) {
        if (weightMap.isEmpty()) {
            Map<String, Integer> map = ExcelFrequencyLoad.load();
            weightMap.putAll(map);
        }
        return weightMap.getOrDefault(ch, 45);
    }

    /**
     * 三字词
     *
     * <p>构词规则：第一字打第一码，第二字打一、二两码，第三字打一码（1+2+1）</p>
     *
     * @param text 词组
     * @param stemMap 构词码
     * @param weightMap 词频
     * @param resultSet 输出构词
     * @param formatType 输出格式
     * @since 2018-03-21 14:48:58
     */
    private static void build3CharsCode(String text, Map<String, String> stemMap, Map<String, Integer> weightMap,
            SortedSet<ImeEntry> resultSet, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String ch3 = String.valueOf(chars[2]); // 第三个字
        String stem1 = getStem(stemMap, ch1);
        String stem2 = getStem(stemMap, ch2);
        String stem3 = getStem(stemMap, ch3);
        String code1 = stem1.substring(0, 1);
        String code2 = stem2.substring(0, 2);
        String code3 = stem3.substring(0, 1);

        String code = code1 + code2 + code3;
        int weight = getWeight(weightMap, text);

        ImeDictAnalyzer.addEntryToList(resultSet, text, code, weight, null, "类1", formatType);
    }

    /**
     * 四字及四字以上的词组
     *
     * <p>构词规则：第一、二、三、四字，各打一码（1+1+1+1）</p>
     *
     * @param text 词组
     * @param stemMap 构词码
     * @param weightMap 词频
     * @param resultSet 输出构词
     * @param formatType 输出格式
     * @since 2018-03-21 14:51:16
     */
    private static void build4CharsCode(String text, Map<String, String> stemMap, Map<String, Integer> weightMap,
            SortedSet<ImeEntry> resultSet, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String ch3 = String.valueOf(chars[2]); // 第三个字
        String ch4 = String.valueOf(chars[3]); // 第四个字
        String stem1 = getStem(stemMap, ch1);
        String stem2 = getStem(stemMap, ch2);
        String stem3 = getStem(stemMap, ch3);
        String stem4 = getStem(stemMap, ch4);
        String code1 = stem1.substring(0, 1);
        String code2 = stem2.substring(0, 1);
        String code3 = stem3.substring(0, 1);
        String code4 = stem4.substring(0, 1);

        String code = code1 + code2 + code3 + code4;
        int weight = getWeight(weightMap, text);

        ImeDictAnalyzer.addEntryToList(resultSet, text, code, weight, null, "类1", formatType);
    }

    /**
     * 获取用户目录下{@code 文档}目标路径（~/Documents）
     *
     * @since 2024-05-12 19:23
     */
    private static String getDocPath() {
        String userHome = System.getProperty("user.home");
        return userHome + File.separatorChar + "Documents";
    }

}
