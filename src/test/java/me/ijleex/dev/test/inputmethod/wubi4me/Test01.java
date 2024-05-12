/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod.wubi4me;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import me.ijleex.dev.test.inputmethod.ImeDictAnalyzer;
import me.ijleex.dev.test.inputmethod.entry.DDImeEntry;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;

/**
 * 将多多输入法原始码表文件处理成可以使用 MySQL “LOAD DATA INFILE” 语法加载数据到数据库的文件
 * <p>多多输入法原始词条和 MySQL 加载的数据分别如下：</p>
 * <pre>
 *     工戈草头右框七	aaaa#序1024
 *             ↓ ↓
 *     aaaa	工戈草头右框七	1024	#类1
 * </pre>
 * 使用 TAB 分隔。
 *
 * @author liym
 * @version 2018-05-17 21:09 extends ImeDictAnalyzer
 * @since 2017-08-06 20:55 新建
 */
public class Test01 {

    /**
     * 输入法词库目录
     *
     * <p>~/Documents/InputMethod/[新世纪五笔]/原始码表/</p>
     */
    private static final String DICT_PATH = "InputMethod/[新世纪五笔]/原始码表";

    /**
     * 初始设置
     *
     * @version @BeforeAll method 'public void com.honeybees.intellij.duouoime.wubi4me.Test01.initSetup()' must be static unless the test class is annotated with @TestInstance(Lifecycle.PER_CLASS).
     * @version 2018-03-14 11:17:16 使用 System.getProperty("user.home") 获取用户目录
     * @since 2017-08-09 10:13
     */
    @BeforeAll
    public static void initSetup() {
        // 方便使用 VIM 替换（%s/\t-//g） 2017-08-29 21:26:57
        ImeDictAnalyzer.addEntryFile("01.五笔-主码-常用字.txt", "-");
        ImeDictAnalyzer.addEntryFile("02.五笔-主码-词组.txt", "-#类1");
        ImeDictAnalyzer.addEntryFile("03.五笔-主码-快捷符号.txt", "-#类2");
        ImeDictAnalyzer.addEntryFile("04.五笔-主码-命令直通车.txt", "-#类3");
        ImeDictAnalyzer.addEntryFile("05.五笔-次显-生僻字.txt", "-#次");
        ImeDictAnalyzer.addEntryFile("06.五笔-主码-用户词.txt", "-#用");
        ImeDictAnalyzer.addEntryFile("07.五笔-辅码-拼音.txt", "#辅");

        // 设置分行符（为 UNIX）2018-03-20 14:50:54
        System.setProperty("line.separator", "\n");
    }

    /**
     * 五笔词库
     *
     * @throws IOException 读/写文件错误
     * @version 2018-03-20 20:33:05 经测试，虽然 buildWubiImeData(..) 方法添加了一个参数，导致多了一些判断，
     * 但是整体的执行时间却减少了。
     * @since 2017-08-06 21:00
     */
    @Test
    public void testWubi() throws IOException {
        String[] files = {"01.五笔-主码-常用字.txt",
                "02.五笔-主码-词组.txt",
                "03.五笔-主码-快捷符号.txt",
                "04.五笔-主码-命令直通车.txt",
                "05.五笔-次显-生僻字.txt",
                "06.五笔-主码-用户词.txt"};
        ImeDictAnalyzer.analyzeDict(DICT_PATH, files, "1.t_ime_dict.txt");
    }

    /**
     * 拼音词库
     *
     * @throws IOException 读/写文件错误
     * @since 2017-08-07 18:08 新建
     */
    @Test
    public void testPinyin() throws IOException {
        String[] files = {"07.五笔-辅码-拼音.txt"};
        ImeDictAnalyzer.analyzeDict(DICT_PATH, files, "2.t_ime_dict_py.txt");
    }

    /**
     * 处理词库文件：排序并重新设置词条的排序编号
     *
     * <p>排序：根据词频倒序排序，同一个词条，词频（编号）相同的，顺序不变。比如拼音为 shu 的词条，词频全是1024，则按原来的顺序输出。</p>
     * <pre>
     * $ddcmd(书,书[nnh])	shu#序1024
     * $ddcmd(树,树[scf])	shu#序1024
     * ……
     * </pre>
     *
     * <p>对已排序的词条重新设置排序编号：将所有的词条按拼音分组，再对分组的词条重新设置降序的排序编号。</p>
     *
     * <p><b>输出格式与读取文件的格式相同（即多多输入法格式）：{@link DDImeEntry#toString()}</b></p>
     *
     * @throws IOException 读/写文件错误
     * @version 2017-08-23 10:38:53 用来处理拼音词库
     * @version 2018-03-20 11:17:30 从已删除的 Test02.java 类移过来
     * @version 2018-03-20 13:41:42 修改 buildMySQLLoadData() 方法的签名及定义，以适用于该方法
     * @version 2018-05-17 21:45:56 调用 {@link  ImeDictAnalyzer#reArrangeDict} 方法
     * @see DDImeEntry#toString()
     * @see ImeEntry#equals(Object)
     * @see ImeDictAnalyzer#reArrangeDict(String, String, boolean, int)
     * @since 2017-08-09 16:30
     */
    @Test
    public void testRearrange() throws IOException {
        String srcFilename = "07.五笔-辅码-拼音.txt"; // 要处理的文件
        ImeDictAnalyzer.reArrangeDict(DICT_PATH, srcFilename, true, 4096);
    }

}
