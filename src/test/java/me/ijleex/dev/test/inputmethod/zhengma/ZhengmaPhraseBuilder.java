/*
 * Copyright 2011-2018 ijym-lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.ijleex.dev.test.inputmethod.zhengma;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ijleex.dev.test.inputmethod.FormatType;
import me.ijleex.dev.test.inputmethod.ImeDictAnalyzer;
import me.ijleex.dev.test.inputmethod.Utils;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 郑码构词 工具类
 *
 * @author liym
 * @since 2018-06-06 09:13:20 新建
 */
public class ZhengmaPhraseBuilder {

    /**
     * 输出格式
     *
     * @since 2018-03-21 15:54:38
     */
    private static final FormatType OUT_FORMAT = FormatType.DuoDuo;

    /**
     * 构词码表
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
    private static final String OUT_FILE = "D:/ProgramFiles/MySQL/mysql-8.0.19-winx64/docs/ctor-result.txt";

    /**
     * 初始化
     *
     * @throws java.io.IOException 读/写文件错误
     * @since 2018-03-21 13:44:48
     */
    @BeforeClass
    public static void initSetup() throws IOException {
        String userHome = System.getProperty("user.home");

        // 加载构词码
        String stemFile = "/Documents/InputMethod/[超集郑码]/原始码表/构词码.txt";
        Path srcPath = Paths.get(userHome, stemFile);
        List<String> codeList = Files.readAllLines(srcPath);
        System.out.println("行数：" + codeList.size());
        for (String line : codeList) {
            String[] linePair = Utils.tokenizeToStringArray(line, "\t"); // 一	av
            String han = linePair[0]; // 汉字
            String code = linePair[1]; // 构词码
            CTOR_STEM_CODE.put(han, code);
        }
        System.out.println("单字数：" + CTOR_STEM_CODE.size());

        // 加载 要构词的词组 2018-06-25 16:08:00
        String srcPhrasesFile = "/Documents/InputMethod/[超集郑码]/原始码表/构词源.txt";
        Path filePath = Paths.get(userHome, srcPhrasesFile);
        List<String> srcPhrasesList = Files.readAllLines(filePath);
        CTOR_SRC_PHRASES.addAll(srcPhrasesList);
    }

    /**
     * 郑码构词
     *
     * <p>
     * 郑码词组规则：
     * <li>两字词：取第一个字的首根和次根各 1 码，取第二个字的首根和次根各 1 码（2+2）</li>
     * <li>三字词：取第一字首根，第二字的首根、次根，第三字的首根（1+2+1）</li>
     * <li>四字词：各取每字的第一字根（1+1+1+1）</li>
     * <li>四字以上词：取前面四字首码（1+1+1+1）</li>
     * </p>
     *
     * @throws java.io.IOException 读/写文件错误
     * @since 2018-06-06 09:18:14
     */
    @Test
    public void buildZhengmaPhrase01() throws IOException {
        List<ImeEntry> outList = new ArrayList<>(60);

        for (String text : CTOR_SRC_PHRASES) {
            int length = text.length();
            if (length == 2) { // 二字词
                build2CharsCode(outList, text, OUT_FORMAT);
            } else if (length == 3) { // 三字词
                build3CharsCode(outList, text, OUT_FORMAT);
            } else if (length >= 4) { // 四字词及四字以上的词组
                build4CharsCode(outList, text, OUT_FORMAT);
            }
        }

        int size = outList.size();
        System.out.println("词条总数：" + size);

        Path dstPath = Paths.get(OUT_FILE);
        Files.write(dstPath, outList);
        System.out.println(dstPath);
    }

    /**
     * 二字词
     *
     * <p>构词规则：每个字各打一、二两码（2+2）</p>
     *
     * @param text 词组
     * @since 2018-03-21 13:39:49
     */
    private void build2CharsCode(List<ImeEntry> outList, String text, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String code1 = getCode(ch1);
        String code2 = getCode(ch2);
        String stem1 = code1.substring(0, 2);
        String stem2 = code2.substring(0, 2);

        String code = stem1 + stem2;

        ImeDictAnalyzer.addEntryToList(outList, code, text, "45", "-#类1", formatType);
    }

    /**
     * 根据汉字获取代码（构词码）
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
     * <p>构词规则：第一打第一码，第二字打一、二两码，第三字打一码（1+2+1）</p>
     *
     * @param text 词组
     * @since 2018-03-21 14:48:58
     */
    private void build3CharsCode(List<ImeEntry> outList, String text, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String ch3 = String.valueOf(chars[2]); // 第三个字
        String code1 = getCode(ch1);
        String code2 = getCode(ch2);
        String code3 = getCode(ch3);
        String stem1 = code1.substring(0, 1);
        String stem2 = code2.substring(0, 2);
        String stem3 = code3.substring(0, 1);

        String code = stem1 + stem2 + stem3;

        ImeDictAnalyzer.addEntryToList(outList, code, text, "45", "-#类1", formatType);
    }

    /**
     * 四字及四字以上的词组
     *
     * <p>构词规则：第一、二、三、四字，各打一码（1+1+1+1）</p>
     *
     * @param text 词组
     * @since 2018-03-21 14:51:16
     */
    private void build4CharsCode(List<ImeEntry> outList, String text, FormatType formatType) {
        char[] chars = text.toCharArray();
        String ch1 = String.valueOf(chars[0]); // 第一个字
        String ch2 = String.valueOf(chars[1]); // 第二个字
        String ch3 = String.valueOf(chars[2]); // 第三个字
        String ch4 = String.valueOf(chars[3]); // 第四个字
        String code1 = getCode(ch1);
        String code2 = getCode(ch2);
        String code3 = getCode(ch3);
        String code4 = getCode(ch4);
        String stem1 = code1.substring(0, 1);
        String stem2 = code2.substring(0, 1);
        String stem3 = code3.substring(0, 1);
        String stem4 = code4.substring(0, 1);

        String code = stem1 + stem2 + stem3 + stem4;

        ImeDictAnalyzer.addEntryToList(outList, code, text, "45", "-#类1", formatType);
    }

}
