/*
 * Copyright 2011-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.UnicodeSet;

import me.ijleex.dev.test.inputmethod.cncorpus.ExcelFrequencyLoad;
import me.ijleex.dev.test.inputmethod.entry.DBLoadEntry;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;
import me.ijleex.dev.test.inputmethod.entry.RimeEntry;
import me.ijleex.dev.test.inputmethod.entry.SQLInsertEntry;
import me.ijleex.dev.test.inputmethod.entry.SQLUpdateEntry;
import me.ijleex.dev.test.inputmethod.zhengma.ZhengmaPhraseBuilder;
import me.ijleex.dev.test.unihan.CodePointUtil;

/**
 * 输入法词库解析
 *
 * @author liym
 * @since 2018-05-16 10:51 新建
 */
public final class ImeDictAnalyzer {

    /**
     * 设置码表文件中的词条的类型，可用的类型有 <b>{@code -}、{@code 类1}、{@code 类2}、…、{@code 次}、{@code 用}、{@code 辅}</b> 等，用于多多输入法
     */
    private static final Map<String, String> FILE_TYPE = new LinkedHashMap<>(7);

    /**
     * 构词码
     *
     * @since 2024-05-14 00:54
     */
    private static final Map<String, String> STEM_MAP = new HashMap<>(27660);

    /**
     * CJK E、F及兼容区字
     *
     * @since 2019-08-26 17:21
     */
    private static final UnicodeSet CJK_E_F_COMP = new UnicodeSet();

    /**
     * @since 2018-03-20 14:09:40 MySQL 临时目录
     */
    private static final String OUT_PATH = "D:/ProgramFiles/MySQL/mysql-8.3.0-winx64/docs/";

    private static final Logger logger = LoggerFactory.getLogger(ImeDictAnalyzer.class);

    private ImeDictAnalyzer() {
        throw new IllegalAccessError("No instance");
    }

    /**
     * 加载输入法词条数据（使用 多多输入法原始码表格式 保存）
     *
     * <p><b>一个词条的格式为：{@code 一	a	81694	av}</b> （Tab 分隔） </p>
     *
     * <p>可以输出为用于 MySQL “LOAD DATA INFILE” 命令加载的文件的数据；<br/>
     * 或者输出为多多输入法原始格式数据。</p>
     *
     * <p>MySQL “LOAD DATA INFILE” 命令执行详情见 wubi-ime.sql</p>
     *
     * @param dictPath 词库文件路径
     * @param fileName 文件名称
     * @param entrySet 文件中的词条列表
     * @param formatType 输出数据的格式
     * @throws IOException 读/写文件错误
     * @see ImeEntry#toString()
     * @since 2017-08-07 17:37
     */
    public static void loadImeDictData(String dictPath, String fileName, SortedSet<ImeEntry> entrySet, FormatType formatType)
            throws IOException {
        Path filePath = Paths.get(getDocPath(), dictPath, fileName);
        List<String> lineList = Files.readAllLines(filePath);
        logger.info("文件行数：{} \t\t {}", fileName, lineList.size());

        String type = FILE_TYPE.get(fileName);
        parseEntry(lineList, type, entrySet, formatType);
    }

    /**
     * @since 2018-05-25 14:16
     */
    private static void parseEntry(List<String> lineList, String type, SortedSet<ImeEntry> outList, FormatType formatType) {
        for (String line : lineList) { // 一	a	81694	av
            // System.out.println(line);

            if (line.startsWith("---config@")) {
                continue;
            }

            // 修改分隔符为：\t#，即去除空格 2018-06-01 10:22:07
            String[] data = Utils.tokenizeToStringArray(line, "\t");
            if (data.length == 4) {
                String text = data[0]; // 一
                String code = data[1]; // a
                String weight = data[2]; // 81694
                String stem = data[3]; // av

                addEntryToList(outList, text, code, Integer.parseInt(weight), stem, type, formatType);
            } else if (data.length == 3) {
                String text = data[0]; // 一
                String code = data[1]; // a
                String weight = data[2]; // 81694

                addEntryToList(outList, text, code, Integer.parseInt(weight), null, type, formatType);
            } else if (data.length == 2) {
                String text = data[0]; // 言
                String code = data[1]; // yyyy

                addEntryToList(outList, text, code, 0, null, type, formatType);
            } else if (data.length == 1) {
                // 若只有一个元素，则只能是汉字 2018-06-16 09:55:11
                String text = data[0]; // 言

                addEntryToList(outList, text, null, 0, null, type, formatType);
            }
        }
    }

    /**
     * Add wubi/zhengma entry to entrySet
     *
     * @param entrySet List
     * @param text 词条
     * @param code 编码
     * @param weight 词频
     * @param stem 造词码
     * @param type 分类
     * @param formatType 输出数据的格式
     * @since 2018-04-21 17:33 从 inputmethod.wubi4me.Test01 类中移过来的
     */
    public static void addEntryToList(SortedSet<ImeEntry> entrySet,
            String text, String code, int weight, String stem, String type, FormatType formatType) {
        ImeEntry entry = new ImeEntry(text, code);
        if (FormatType.RIME == formatType) {
            if ((stem == null) && "-".equals(type) || "次".equals(type)) {
                stem = getStem(text);
            }
            entry = new RimeEntry(text, code, weight, stem, type);
        } else if (FormatType.DB_DATA_LOAD == formatType) {
            entry = new DBLoadEntry(text, code, weight, stem, type);
        } else if (FormatType.SQL_INSERT == formatType) {
            entry = new SQLInsertEntry(text, code, weight, stem, type);
        } else if (FormatType.SQL_UPDATE == formatType) {
            entry = new SQLUpdateEntry(text, code, weight, stem, type);
        }
        entrySet.add(entry);
    }

    /**
     * 解析詞庫
     *
     * @param dictFiles 要解析的詞庫的文件名稱
     * @param outFilename 輸出文件按名稱
     * @throws java.io.IOException 读/写文件错误
     * @since 2018-05-17 12:55
     */
    public static void analyzeDict(String dictPath, String[] dictFiles, String outFilename) throws IOException {
        SortedSet<ImeEntry> entrySet = new TreeSet<>();
        for (String file : dictFiles) {
            loadImeDictData(dictPath, file, entrySet, FormatType.DB_DATA_LOAD);
        }
        logger.info("词条总数：{}", entrySet.size());

        Path dstPath = Paths.get(OUT_PATH, outFilename);
        Files.write(dstPath, entrySet);
        System.out.println(dstPath);
    }

    /**
     * 词条重新排序，支持重新设置词条的词频
     *
     * <p><b>输出格式与读取文件的格式相同（即Rime输入法格式）：{@link RimeEntry#toString()}</b></p>
     *
     * @param dictPath 词库文件路径
     * @param fileName 要處理的文件的名稱
     * @param resetWeight 是否重新設置詞頻
     * @param defaultWeight 默认词频
     * @throws IOException 读/写文件错误
     * @version 2017-08-23 10:38:53 用来处理拼音词库
     * @version 2018-03-20 11:17:30 从已删除的 Test02.java 类移过来
     * @version 2018-03-20 13:41:42 修改 buildMySQLLoadData() 方法的签名及定义，以适用于该方法
     * @version 2018-07-06 09:29 添加参数：defaultWeight
     * @since 2018-05-17 21:30
     */
    public static void reArrangeDict(String dictPath, String fileName, boolean resetWeight, int defaultWeight)
            throws IOException {
        SortedSet<ImeEntry> entrySet = new TreeSet<>();

        loadImeDictData(dictPath, fileName, entrySet, FormatType.RIME); // 输出为多多格式 2018-03-20 13:37:20
        logger.info("已加载词条总数：{}", entrySet.size());

        // 对已排序的词条重新设置编号 2017-08-29 17:50:25
        if (resetWeight) {
            // 从《现代汉语语料库词频表.xlsx》中加载词频 2024-05-12 17:20:17
            Map<String, Integer> weightMap = ExcelFrequencyLoad.load();
            entrySet.forEach(entry -> resetWeight(entry, weightMap, defaultWeight));
            logger.info("已重新设置词频");
        }

        // 将 List 的数据写入到文件中
        Path dstPath = Paths.get(OUT_PATH, fileName);
        Files.write(dstPath, entrySet);
        logger.info("已写入文件：{}", dstPath);
    }

    /**
     * 设置 {@link ImeEntry#getWeight() 词频}
     *
     * @param entry 词条
     * @param weightMap 词频列表
     * @param defaultWeight 默认词频
     * @version 2018-05-28 18:20 改为使用海峰词频
     * @version 2018-05-30 20:56 改为使用 “词频-语料库.txt”
     * @since 2018-07-06 09:28 添加参数：defaultWeight
     * @since 2017-08-29 17:56
     */
    private static void resetWeight(ImeEntry entry, Map<String, Integer> weightMap, int defaultWeight) {
        // 从 词频列表 中获取词条的词频；不存在的词条，设置词频为10 2018-05-28 18:17:09
        // 使用 语料库词频：该词频中，最小词频为 51；不存在的字使用默认词频，常用字的默认词频为 50，词组 45，
        // 生僻字（CJK-A,B,C,D）10，E、F及兼容区为 0，不是 CJK-Unified 的字为 -1 2019-08-25 22:41:40
        String text = entry.getText();
        Integer weight = weightMap.get(text);
        if (weight == null) {
            // 如果词频表中不存在这个字，先判断该字是不是 CJK-Unified 字，如果是，则使用 defaultWeight，否则 -1
            weight = CodePointUtil.isCJKUnifiedIdeographicChar(text) ? defaultWeight : -1;
            if (isCJKEFCompChar(text)) {
                weight = 0;
            }
        } else {
            if (weight != entry.getWeight()) {
                logger.warn("词频不相等：text={}、获取词频={}", entry, weight);
            }
        }
        entry.setWeight(weight);
    }

    /**
     * 是否是 CJK E、F及兼容区字
     *
     * @since 2019-08-26 17:08
     */
    private static boolean isCJKEFCompChar(String ch) {
        if (CJK_E_F_COMP.isEmpty()) {
            // CJK Unified Ideographs Extension E
            CJK_E_F_COMP.add(0x2B820, 0x2CEA1); // 5,762

            // CJK Unified Ideographs Extension F
            CJK_E_F_COMP.add(0x2CEB0, 0x2EBE0); // 7,473

            // CJK Compatibility Ideographs (中日韩兼容表意文字)
            CJK_E_F_COMP.add(0xFA0E); // 12 Unified
            CJK_E_F_COMP.add(0xFA0F);
            CJK_E_F_COMP.add(0xFA11);
            CJK_E_F_COMP.add(0xFA13);
            CJK_E_F_COMP.add(0xFA14);
            CJK_E_F_COMP.add(0xFA1F);
            CJK_E_F_COMP.add(0xFA21);
            CJK_E_F_COMP.add(0xFA23);
            CJK_E_F_COMP.add(0xFA24);
            CJK_E_F_COMP.add(0xFA27);
            CJK_E_F_COMP.add(0xFA28);
            CJK_E_F_COMP.add(0xFA29);
        }

        return CJK_E_F_COMP.contains(ch);
    }

    /**
     * 构建 词条-编码 对应关系
     *
     * <p>怋=[uyhd]</p>
     *
     * @param entrySet 词条列表，不能为空
     * @return 包含 词条-编码 对应关系的Map
     * @since 2018-08-27 14:12
     */
    public static Map<String, List<String>> convertEntryListToTextMap(SortedSet<ImeEntry> entrySet) {
        //if (entrySet == null || entrySet.isEmpty()) {return Collections.emptyMap();}

        // 构建 词条-编码 对应关系
        Map<String, List<String>> textMap = new HashMap<>(entrySet.size() - 200);
        for (ImeEntry entry : entrySet) {
            String text = entry.getText();
            String code = entry.getCode();

            List<String> codeList = textMap.get(text);

            // 该词条不存在
            if (codeList == null) {
                // 一个词条，最多3个编码
                codeList = new ArrayList<>(3);
                codeList.add(code);
                textMap.put(text, codeList);
            } else {
                // 该词条已存在，且有多个编码
                int idx = codeList.indexOf(code);
                if (idx >= 0) {
                    // 该词条有重复的编码，则输出
                    logger.warn("重复词条：{}", entry);
                } else {
                    codeList.add(code);
                }
            }
        }
        return textMap;
    }

    /**
     * 添加词库文件及其类型
     *
     * @param fileName 文件名称
     * @param type 对应类型
     * @since 2024-05-12 19:36
     */
    public static void addEntryFile(String fileName, String type) {
        FILE_TYPE.put(fileName, type);
    }

    /**
     * 获取郑码单字构词码
     *
     * @param text 单字
     * @return 单字对应的构词破
     * @since 2024-05-14 00:56
     */
    public static String getStem(String text) {
        if (STEM_MAP.isEmpty()) {
            Map<String, String> stemMap = ZhengmaPhraseBuilder.loadStemMap();
            STEM_MAP.putAll(stemMap);
        }
        return STEM_MAP.get(text);
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
