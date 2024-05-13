/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.cncorpus;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;

import me.ijleex.dev.test.inputmethod.cncorpus.entry.FrequencyEntry;
import me.ijleex.dev.test.inputmethod.cncorpus.entry.FrequencyExtEntry;

/**
 * 从《现代汉语语料库词频表.xlsx》中加载词频.
 *
 * @author liym
 * @since 2024-04-30 22:23 新建
 */
public final class ExcelFrequencyLoad {

    private ExcelFrequencyLoad() {
        throw new InstantiationError("No instance");
    }

    /**
     * 加载词频
     *
     * <p>现代汉语语料库词语频率表 下载自 www.cncorpus.org 语料库在线网站 2018-05-30 20:50:42</p>
     * <p>语料规模：2000万字，只列入出现次数大于50次的词</p>
     */
    public static Map<String, Integer> load() {
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

        return weightMap;
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
