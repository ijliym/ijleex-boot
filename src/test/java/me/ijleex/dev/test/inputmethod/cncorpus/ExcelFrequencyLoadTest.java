/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.cncorpus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.EasyExcel;

import me.ijleex.dev.test.inputmethod.cncorpus.entry.FrequencyEntry;
import me.ijleex.dev.test.inputmethod.cncorpus.entry.FrequencyExtEntry;

/**
 * 词频加载测试.
 *
 * @author liym
 * @since 2024-04-30 22:30 新建
 */
public class ExcelFrequencyLoadTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void load1() {
        Path excelFile = Paths.get(getDocPath(), "InputMethod/汉字相关/语料库字词频/现代汉语语料库词频表.xlsx");
        Map<String, Integer> entryMap = new HashMap<>(15000);
        EasyExcel.read(excelFile.toFile(), FrequencyEntry.class,
                        new ExcelFrequency1Listener(entryMap))
                .sheet(0) // 表格#1
                .headRowNumber(7)
                .doRead();
        logger.info("词频：为={}", entryMap.get("为"));
    }

    @Test
    public void load2() {
        Path excelFile = Paths.get(getDocPath(), "InputMethod/汉字相关/语料库字词频/现代汉语语料库词频表.xlsx");
        Map<String, Integer> entryMap = new HashMap<>(15000);
        EasyExcel.read(excelFile.toFile(), FrequencyExtEntry.class,
                        new ExcelFrequency2Listener(entryMap))
                .sheet(1) // 表格#2
                .headRowNumber(7)
                .doRead();
        logger.info("词频：为={}", entryMap.get("为"));
        logger.info("词频：一={}", entryMap.get("一"));
    }

    @Test
    public void interPrint() throws IOException {
        Path excelFile = Paths.get(getDocPath(), "InputMethod/汉字相关/语料库字词频/现代汉语语料库词频表.xlsx");

        Map<String, Integer> entry1Map = new HashMap<>(15000);
        EasyExcel.read(excelFile.toFile(), FrequencyEntry.class,
                        new ExcelFrequency1Listener(entry1Map))
                .sheet(0) // 表格#1
                .headRowNumber(7)
                .doRead();

        Map<String, Integer> entry2Map = new HashMap<>(15000);
        List<String> ignoreList = new ArrayList<>(200);
        EasyExcel.read(excelFile.toFile(), FrequencyExtEntry.class,
                        new ExcelFrequency2Listener(entry2Map, ignoreList))
                .sheet(1) // 表格#2
                .headRowNumber(7)
                .doRead();

        // ===

        List<String> msgList = new ArrayList<>();
        msgList.add("现代汉语语料库分词类词频表 中不存在的词语：");
        logger.info("现代汉语语料库分词类词频表 中不存在的词语：");
        for (Map.Entry<String, Integer> entry : entry1Map.entrySet()) {
            String text = entry.getKey();
            if (ignoreList.contains(text)) { // 过滤人名
                continue;
            }
            Integer weight = entry.getValue();
            if (!entry2Map.containsKey(text)) {
                logger.warn("{}={}", text, weight);
                msgList.add(text + '\t' + weight);
            }
        }

        msgList.add("");
        msgList.add("现代汉语语料库词频表 中不存在的词语：");
        logger.info("现代汉语语料库词频表 中不存在的词语：");
        for (Map.Entry<String, Integer> entry : entry2Map.entrySet()) {
            String text = entry.getKey();
            Integer weight = entry.getValue();
            if (!entry1Map.containsKey(text)) {
                logger.warn("{}={}", text, weight);
                msgList.add(text + '\t' + weight);
            }
        }

        Path outFile = Paths.get(getDocPath(), "out.txt");
        Files.write(outFile, msgList);
    }

    /**
     * 合并词频
     */
    @Test
    public void unionFrequency() {
        Path excelFile = Paths.get(getDocPath(), "InputMethod/汉字相关/语料库字词频/现代汉语语料库词频表.xlsx");

        Map<String, Integer> entry1Map = new HashMap<>(15000);
        EasyExcel.read(excelFile.toFile(), FrequencyEntry.class,
                        new ExcelFrequency1Listener(entry1Map))
                .sheet(0) // 表格#1
                .headRowNumber(7)
                .doRead();

        Map<String, Integer> entry2Map = new HashMap<>(15000);
        List<String> ignoreList = new ArrayList<>(200);
        EasyExcel.read(excelFile.toFile(), FrequencyExtEntry.class,
                        new ExcelFrequency2Listener(entry2Map, ignoreList))
                .sheet(1) // 表格#2
                .headRowNumber(7)
                .doRead();

        // ===

        // 合并词频
        Map<String, Integer> weightMap = new HashMap<>(15000);

        for (Map.Entry<String, Integer> entry : entry1Map.entrySet()) {
            String text = entry.getKey();
            if (ignoreList.contains(text)) { // 过滤人名
                continue;
            }
            Integer weight1 = entry.getValue();
            Integer weight2 = entry2Map.get(text);
            if (weight2 == null) {
                weightMap.put(text, weight1);
            } else {
                weightMap.put(text, Integer.max(weight1, weight2));
            }
        }

        for (Map.Entry<String, Integer> entry : entry2Map.entrySet()) {
            String text = entry.getKey();
            ///if (ignoreList.contains(text)) { // 过滤人名
            ///    continue;
            ///}
            Integer weight1 = entry.getValue();
            Integer weight2 = entry1Map.get(text);
            if (weight2 == null) {
                weightMap.put(text, weight1);
            } else {
                weightMap.put(text, Integer.max(weight1, weight2));
            }
        }

        logger.info("合并后词频数：{}", weightMap.size());

        logger.info("词频：的={}、为={}", weightMap.get("的"), weightMap.get("为"));
        logger.info("词频：毛主席={}、光绪={}", weightMap.get("毛主席"), weightMap.get("光绪"));
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
