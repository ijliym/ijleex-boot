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
import java.util.ArrayList;
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
 * Test01
 *
 * @author liym
 * @version 2018-05-17 21:58 extends ImeDictAnalyzer
 * @since 2018-04-21 17:05 新建
 */
public class Test01 {

    /**
     * 输入法词库目录
     *
     * <p>~/Documents/InputMethod/[超集郑码]/原始码表/</p>
     */
    private static final String DICT_PATH = "InputMethod/[超集郑码]/原始码表";

    /**
     * 初始设置
     *
     * @since 2018-04-21 18:08:20
     */
    @BeforeAll
    public static void initSetup() {
        // 方便使用 VIM 替换（%s/\t-//g） 2017-08-29 21:26:57
        // 原始码表 2018-04-25 20:31:51
        ImeDictAnalyzer.addEntryFile("01.郑码-主码-常用字.txt", "-");
        ImeDictAnalyzer.addEntryFile("02.郑码-主码-词组.txt", "-#类1");
        ImeDictAnalyzer.addEntryFile("03.郑码-主码-快捷符号.txt", "-#类2");
        ImeDictAnalyzer.addEntryFile("04.郑码-主码-命令直通车.txt", "-#类3");
        ImeDictAnalyzer.addEntryFile("05.郑码-次显-生僻字.txt", "-#次");
        ImeDictAnalyzer.addEntryFile("06.郑码-主码-用户词.txt", "-#用");

        // 设置分行符（为 UNIX）2018-03-20 14:50:54
        System.setProperty("line.separator", "\n");
    }

    /**
     * 郑码词库
     *
     * @throws IOException 读/写文件错误
     * @version 2018-05-17 22:05 {@link ImeDictAnalyzer#analyzeDict(String, String[], String)}
     * @since 2018-04-21 17:21:35s
     */
    @Test
    public void testZhengma() throws IOException {
        String[] files = {"01.郑码-主码-常用字.txt",
                "02.郑码-主码-词组.txt",
                "03.郑码-主码-快捷符号.txt",
                "04.郑码-主码-命令直通车.txt",
                "05.郑码-次显-生僻字.txt",
                "06.郑码-主码-用户词.txt"};
        ImeDictAnalyzer.analyzeDict(DICT_PATH, files, "1.t_ime_dict-zm.txt");
    }

    /**
     * 处理词库文件：排序并重新设置词条的排序编号
     *
     * @throws IOException 读/写文件错误
     * @see ImeDictAnalyzer#resetWeight(ImeEntry, Map, int)
     * @since 2018-05-17 22:04
     */
    @Test
    public void testRearrange() throws IOException {
        ImeDictAnalyzer.reArrangeDict(DICT_PATH, "01.郑码-主码-常用字.txt", true, 50);
        ImeDictAnalyzer.reArrangeDict(DICT_PATH, "02.郑码-主码-词组.txt", true, 45);
        ImeDictAnalyzer.reArrangeDict(DICT_PATH, "05.郑码-次显-生僻字.txt", true, 10);
    }

    /**
     * 找出编码不同的生僻字
     *
     * @throws IOException 读/写文件错误
     * @since 2018-08-27 18:05
     */
    @Test
    public void testDiff() throws IOException {
        SortedSet<ImeEntry> set1 = new TreeSet<>();
        SortedSet<ImeEntry> set2 = new TreeSet<>();

        ImeDictAnalyzer.loadImeDictData(DICT_PATH, "05.郑码-次显-生僻字.txt", set1, FormatType.DuoDuo);
        ImeDictAnalyzer.loadImeDictData(DICT_PATH, "../05.郑码-次显-生僻字_删除的编码.txt", set2, FormatType.DuoDuo);

        // 构建 词条-编码 对应关系的
        Map<String, List<String>> textMap = ImeDictAnalyzer.convertEntryListToTextMap(set1);

        // 要保留的词条
        List<ImeEntry> toRetainedList = new ArrayList<>(3400);
        for (ImeEntry entry : set2) {
            String text = entry.getText();
            String code = entry.getCode();

            List<String> codeList = textMap.get(text);
            if (codeList == null) {
                // 该词条在“原始码表/05.郑码-次显-生僻字.txt”文件中不存在
                //System.out.println("不存在的词条：" + entry);
                toRetainedList.add(entry);
                continue;
            }
            codeList.forEach(inUseCode -> {
                // For debug
                if ("\uD855\uDE8A".equals(text)) {
                    System.out.println("Debug ==> " + entry);
                }
                if (inUseCode.startsWith(code) || code.startsWith(inUseCode)) {
                    // 过滤三简或相同编码的词条
                    System.out.println("重复的词条：" + entry);
                } else {
                    toRetainedList.add(entry);
                }
            });
        }
        System.out.println("toRetainedList size: " + toRetainedList.size());

        Path dstPath = Paths.get("D:/to-retained-entry.txt");
        Files.write(dstPath, toRetainedList);
        System.out.println("已写入文件：\n" + dstPath);
    }

}
