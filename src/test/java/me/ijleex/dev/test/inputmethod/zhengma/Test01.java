/*
 * Copyright 2011-2020 the original author or authors.
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
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import me.ijleex.dev.test.inputmethod.ImeDictAnalyzer;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;

/**
 * Test01
 *
 * @author liym
 * @version 2018-05-17 21:58 extends ImeDictAnalyzer
 * @since 2018-04-21 17:05 新建
 */
public class Test01 extends ImeDictAnalyzer {

    /**
     * 初始设置
     *
     * @since 2018-04-21 18:08:20
     */
    @BeforeAll
    public static void initSetup() {
        dictPath = dictPath + "/[超集郑码]/原始码表/";

        String userHome = System.getProperty("user.home");
        dictPath = userHome + dictPath;

        // 方便使用 VIM 替换（%s/\t-//g） 2017-08-29 21:26:57
        // 原始码表 2018-04-25 20:31:51
        FILE_TYPE.put("01.郑码-主码-常用字.txt", "-");
        FILE_TYPE.put("02.郑码-主码-词组.txt", "-#类1");
        FILE_TYPE.put("03.郑码-主码-快捷符号.txt", "-#类2");
        FILE_TYPE.put("04.郑码-主码-命令直通车.txt", "-#类3");
        FILE_TYPE.put("05.郑码-次显-生僻字.txt", "-#次");
        FILE_TYPE.put("06.郑码-主码-用户词.txt", "-#用");
        FILE_TYPE.put("07.郑码-辅码-拼音.txt", "#辅");

        // 设置分行符（为 UNIX）2018-03-20 14:50:54
        System.setProperty("line.separator", "\n");
    }

    /**
     * 郑码词库
     *
     * @throws IOException 读/写文件错误
     * @version 2018-05-17 22:05 {@link #analyzeDict(String[], int, String)}
     * @since 2018-04-21 17:21:35s
     */
    @Test
    public void testZhengma() throws IOException {
        Set<String> fileSet = FILE_TYPE.keySet();
        fileSet.remove("07.郑码-辅码-拼音.txt");

        String[] files = fileSet.toArray(new String[0]);
        analyzeDict(files, 159381 + 200, "1.t_ime_dict-zm.txt");
    }

    /**
     * 拼音词库
     *
     * @throws IOException 读/写文件错误
     * @since 2018-05-17 22:02:15 新建
     */
    @Test
    public void testPinyin() throws IOException {
        String[] files = {"07.郑码-辅码-拼音.txt"};
        analyzeDict(files, 355457 + 10, "2.t_ime_dict_py.txt");
    }

    /**
     * 处理词库文件：排序并重新设置词条的排序编号
     *
     * @throws IOException 读/写文件错误
     * @see ImeDictAnalyzer#resetWeight(java.util.List, java.util.Map, String)
     * @since 2018-05-17 22:04
     */
    @Test
    public void testRearrange() throws IOException {
        reArrangeDict("01.郑码-主码-常用字.txt", false, "50");
        reArrangeDict("02.郑码-主码-词组.txt", false, "45");
        reArrangeDict("05.郑码-次显-生僻字.txt", false, "10");
    }

    /**
     * 找出编码不同的生僻字
     *
     * @throws IOException 读/写文件错误
     * @since 2018-08-27 18:05
     */
    @Test
    public void testDiff() throws IOException {
        Path path1 = Paths.get(dictPath, "05.郑码-次显-生僻字.txt");
        Path path2 = Paths.get(dictPath, "../05.郑码-次显-生僻字_删除的编码.txt");

        List<ImeEntry> list1 = new ArrayList<>(81419);
        List<ImeEntry> list2 = new ArrayList<>(4438);

        loadImeDictData(path1, list1);
        loadImeDictData(path2, list2);

        // 构建 词条-编码 对应关系的
        Map<String, List<String>> textMap = convertEntryListToTextMap(list1);

        // 要保留的词条
        List<ImeEntry> toRetainedList = new ArrayList<>(3400);
        for (ImeEntry entry : list2) {
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
