/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.zhengma;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ijleex.dev.test.inputmethod.FormatType;
import me.ijleex.dev.test.inputmethod.ImeDictAnalyzer;
import me.ijleex.dev.test.inputmethod.cncorpus.ExcelFrequencyLoad;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;

/**
 * 郑码构词 测试.
 *
 * @author liym
 * @since 2024-05-08 23:26 新建
 */
public class ZhengmaPhraseBuilderTest {

    /**
     * 输入法词库目录
     *
     * <p>~/Documents/InputMethod/[超集郑码]/原始码表/</p>
     */
    private static final String DICT_PATH = "InputMethod/[超集郑码]/原始码表";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public ZhengmaPhraseBuilderTest() {
    }

    @Test
    public void build01() throws IOException {
        // 加载构词码
        String userHome = System.getProperty("user.home");
        String stemFile = "/Documents/InputMethod/[超集郑码]/原始码表/构词码.txt";
        Map<String, String> stemMap = ZhengmaPhraseBuilder.loadStemFile(userHome, stemFile);

        // 加载构词源（要构词的词组） 2024-05-08 23:05:25
        String srcPhrasesFile = "/Documents/InputMethod/[超集郑码]/原始码表/构词源.txt";
        Path filePath = Paths.get(userHome, srcPhrasesFile);
        List<String> srcPhrasesList = Files.readAllLines(filePath);

        SortedSet<ImeEntry> resultSet = ZhengmaPhraseBuilder.build(stemMap, srcPhrasesList);
        logger.info("词条总数：{}", resultSet.size());

        Path resultFile = Paths.get(userHome, "/Documents/郑码构词结果.txt");
        Files.write(resultFile, resultSet);
        logger.info("郑码构词结果：{}", resultFile);
    }

    /**
     * 加载《现代汉语语料库词频表.xlsx》中的词组，使用原始码表中不存在的词语构词后添加到原始码表中
     *
     * @throws IOException 可能发生的异常
     * @since 2024-05-12 19:11
     */
    @Test
    public void build02() throws IOException {
        Map<String, Integer> freqMap = ExcelFrequencyLoad.load();

        SortedSet<ImeEntry> entrySet = new TreeSet<>();
        ImeDictAnalyzer.loadImeDictData(DICT_PATH, "02.郑码-主码-词组.txt", entrySet, FormatType.DuoDuo);
        logger.info("已加载词组：{}", entrySet.size());

        // 得到原始码表中的词组
        Set<String> textSet = new HashSet<>(entrySet.size());
        for (ImeEntry entry : entrySet) {
            textSet.add(entry.getText());
        }

        // 得到需要构词的词组
        Set<String> toBuildWordSet = new HashSet<>(textSet.size());
        for (Map.Entry<String, Integer> entry : freqMap.entrySet()) {
            String text = entry.getKey();
            if (text.length() > 1 && !textSet.contains(text)) {
                toBuildWordSet.add(text);
            }
        }

        if (toBuildWordSet.isEmpty()) {
            logger.warn("需要构词的词组列表为空");
            return;
        }

        // 加载构词码
        String userHome = System.getProperty("user.home");
        String stemFile = "/Documents/InputMethod/[超集郑码]/原始码表/构词码.txt";
        Map<String, String> stemMap = ZhengmaPhraseBuilder.loadStemFile(userHome, stemFile);

        SortedSet<ImeEntry> resultSet = ZhengmaPhraseBuilder.build(stemMap, toBuildWordSet);
        logger.info("已构建词组：{}", resultSet.size());

        // 将新构建的词组添加到原始码表文件中
        entrySet.addAll(resultSet);

        Path dstFile = Paths.get("D:/ProgramFiles/MySQL/mysql-8.3.0-winx64/docs/02.郑码-主码-词组.txt");
        Files.write(dstFile, entrySet);
        logger.info("已写入文件：{}", dstFile);
    }

}
