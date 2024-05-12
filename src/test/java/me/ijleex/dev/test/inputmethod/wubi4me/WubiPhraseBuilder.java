/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.wubi4me;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import me.ijleex.dev.test.inputmethod.FormatType;
import me.ijleex.dev.test.inputmethod.ImeDictAnalyzer;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;

/**
 * 五笔构词 工具类
 *
 * @author liym
 * @since 2018-03-21 13:22 新建
 */
public class WubiPhraseBuilder {

    /**
     * 输出格式
     *
     * @since 2018-03-21 15:54:38
     */
    private static final FormatType OUT_FORMAT = FormatType.MySQL;

    /**
     * 构词码表，不含一级简码，共 27533 个字
     *
     * <p>GB18030-2000收录了27533个汉字</p>
     *
     * @since 2018-03-21 13:43:23 构词用单字编码表，不含一级简码
     */
    private static final Map<String, String> CTOR_STEM_CODE = new HashMap<>(27533);
    /**
     * 要构词的词组
     *
     * @since 2018-06-25 15:53
     */
    private static final List<String> CTOR_SRC_PHRASES = new ArrayList<>(1000);

    /**
     * @since 2018-03-21 16:50 输出词条
     */
    private static final String OUT_FILE = "D:/ProgramFiles/MySQL/mysql-8.0.20-winx64/docs/ctor-result.txt";

    /**
     * 初始化
     *
     * @throws java.io.IOException 读/写文件错误
     * @since 2018-03-21 13:44:48
     */
    @BeforeAll
    public static void initSetup() throws IOException {
        String userHome = System.getProperty("user.home");

        // 构词码表，不含一级简码 2018-06-25 15:59:11
        String stemFile = "/Documents/InputMethod/[新世纪五笔]/原始码表/构词码.txt";
        Path srcPath = Paths.get(userHome, stemFile);
        List<String> codeList = Files.readAllLines(srcPath);
        System.out.println("行数：" + codeList.size());
        for (String line : codeList) {
            String[] linePair = line.split("\t"); // 甲	lhnh
            String han = linePair[0]; // 汉字
            String code = linePair[1]; // 代码
            if (code.length() > 2) { // 只加载代码长度为3和4的词条
                CTOR_STEM_CODE.put(han, code);
            }
        }
        System.out.println("单字数：" + CTOR_STEM_CODE.size());

        // 加载 要构词的词组 2018-06-25 15:51:51
        String srcPhrasesFile = "/Documents/InputMethod/[新世纪五笔]/原始码表/构词源.txt";
        Path filePath = Paths.get(userHome, srcPhrasesFile);
        List<String> srcPhrasesList = Files.readAllLines(filePath);
        CTOR_SRC_PHRASES.addAll(srcPhrasesList);
    }

    /**
     * 五笔构词
     *
     * <p>
     * 五笔词组规则：
     * <li>两字词：每字各取前两字根（2+2）</li>
     * <li>三字词：前两字取首字根，第三字取第一、二字根（1+1+2）</li>
     * <li>四字词：各取每字的第一字根（1+1+1+1）</li>
     * <li>四字以上词：前三字和末字取首字根（1+1+1+末1）</li>
     * </p>
     *
     * @throws java.io.IOException 读/写文件错误
     * @since 2018-03-21 13:25:36
     */
    @Test
    public void buildWubiPhrase01() throws IOException {
        SortedSet<ImeEntry> resultSet = new TreeSet<>();

        for (String text : CTOR_SRC_PHRASES) {
            int length = text.length();
            if (length == 2) { // 二字词
                build2CharsCode(resultSet, text, OUT_FORMAT);
            } else if (length == 3) { // 三字词
                build3CharsCode(resultSet, text, OUT_FORMAT);
            } else if (length >= 4) { // 四字词及四字以上的词组
                build4CharsCode(resultSet, text, OUT_FORMAT);
            }
        }

        int size = resultSet.size();
        System.out.println("词条总数：" + size);

        Path dstPath = Paths.get(OUT_FILE);
        Files.write(dstPath, resultSet);
        System.out.println(dstPath);
    }

    /**
     * 二字词
     *
     * <p>构词规则：每个字各打一、二两码。</p>
     *
     * @param text 词组
     * @since 2018-03-21 13:39:49
     */
    private void build2CharsCode(SortedSet<ImeEntry> resultSet, String text, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String code1 = getCode(ch1);
        String code2 = getCode(ch2);
        String stem1 = code1.substring(0, 2);
        String stem2 = code2.substring(0, 2);

        String code = stem1 + stem2;

        ImeDictAnalyzer.addEntryToList(resultSet, code, text, 4096, "-#类1", formatType);
    }

    /**
     * 根据汉字获取代码
     *
     * @param ch 汉字
     * @return 代码
     * @since 2018-03-21 17:39:50
     */
    private String getCode(String ch) {
        return CTOR_STEM_CODE.get(ch);
    }

    /**
     * 三字词
     *
     * <p>构词规则：第一、二字各打第一码，第三字打一、二两码。</p>
     *
     * @param text 词组
     * @since 2018-03-21 14:48:58
     */
    private void build3CharsCode(SortedSet<ImeEntry> resultSet, String text, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String ch3 = String.valueOf(chars[2]); // 第三个字
        String code1 = getCode(ch1);
        String code2 = getCode(ch2);
        String code3 = getCode(ch3);
        String stem1 = code1.substring(0, 1);
        String stem2 = code2.substring(0, 1);
        String stem3 = code3.substring(0, 2);

        String code = stem1 + stem2 + stem3;

        ImeDictAnalyzer.addEntryToList(resultSet, code, text, 4096, "-#类1", formatType);
    }

    /**
     * 四字及四字以上的词组
     *
     * <p>构词规则：第一、二、三和最后的字，各打一码。</p>
     *
     * @param text 词组
     * @since 2018-03-21 14:51:16
     */
    private void build4CharsCode(SortedSet<ImeEntry> resultSet, String text, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String ch3 = String.valueOf(chars[2]); // 第三个字
        String ch4 = String.valueOf(chars[chars.length - 1]); // 第四个字（或最后一个字）
        String code1 = getCode(ch1);
        String code2 = getCode(ch2);
        String code3 = getCode(ch3);
        String code4 = getCode(ch4);
        String stem1 = code1.substring(0, 1);
        String stem2 = code2.substring(0, 1);
        String stem3 = code3.substring(0, 1);
        String stem4 = code4.substring(0, 1);

        String code = stem1 + stem2 + stem3 + stem4;

        ImeDictAnalyzer.addEntryToList(resultSet, code, text, 4096, "-#类1", formatType);
    }

}
