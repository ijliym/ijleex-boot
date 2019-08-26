/*
 * Copyright 2011-2019 ijym-lee
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

package me.ijleex.dev.test.inputmethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ibm.icu.text.UnicodeSet;

import me.ijleex.dev.test.inputmethod.entry.DDImeEntry;
import me.ijleex.dev.test.inputmethod.entry.ImeEntry;
import me.ijleex.dev.test.inputmethod.entry.MySQLLoadEntry;
import me.ijleex.dev.test.inputmethod.entry.RimeEntry;
import me.ijleex.dev.test.inputmethod.entry.SQLInsertEntry;
import me.ijleex.dev.test.inputmethod.entry.SQLUpdateEntry;
import me.ijleex.dev.test.unihan.CodePointUtil;

/**
 * 输入法词库解析
 *
 * @author liym
 * @since 2018-05-16 10:51 新建
 */
public class ImeDictAnalyzer {

    /**
     * 输入法词库目录
     *
     * <p>~/Documents/InputMethod/[超集郑码]/原始码表/</p>
     * <p>~/Documents/InputMethod/[新世纪五笔]/原始码表/</p>
     */
    protected static String dictPath = "/Documents/InputMethod";
    /**
     * 设置码表文件中的词条的类型，可用的类型有 <b>类1、类2、类3、次、用、辅</b> 等，用于多多输入法
     */
    protected static final Map<String, String> FILE_TYPE = new LinkedHashMap<>(7);

    /**
     * CJK E、F及兼容区字
     *
     * @since 2019-08-26 17:21
     */
    private static final UnicodeSet CJK_E_F_COMP = new UnicodeSet();

    /**
     * @since 2018-03-20 14:09:40 MySQL 临时目录
     */
    private static final String OUT_PATH = "D:/ProgramFiles/MySQL/mysql-8.0.17-winx64/run/";

    /**
     * 加载输入法词条数据（使用 多多输入法原始码表格式 保存）
     *
     * <p><b>一个词条的格式为：的 d#序744863</b> （Tab 分隔） </p>
     *
     * <p>可以输出为用于 MySQL “LOAD DATA INFILE” 命令加载的文件的数据；<br/>
     * 或者输出为多多输入法原始格式数据。</p>
     *
     * <p>MySQL “LOAD DATA INFILE” 命令执行详情见 wubi-ime.sql</p>
     *
     * @param filename 文件名称
     * @param outList List
     * @param formatType 输出数据的格式
     * @throws IOException 读/写文件错误
     * @see ImeEntry#toString()
     * @see DDImeEntry#toString()
     * @since 2017-08-07 17:37
     */
    private void loadImeDictData(String filename, List<ImeEntry> outList, FormatType formatType) throws IOException {
        Path filePath = Paths.get(dictPath, filename);
        List<String> list = Files.readAllLines(filePath);
        System.out.println("行数：" + list.size() + "\t\t" + filename);

        String type = FILE_TYPE.get(filename);
        parseEntry(list, type, outList, formatType);
    }

    /**
     * @since 2018-05-25 14:16
     */
    private void parseEntry(List<String> srcList, String type, List<ImeEntry> outList, FormatType formatType) {
        for (String line : srcList) { // 言<Tab>yyyy#序15944
            // System.out.println(line);

            if (line.startsWith("---config@")) {
                continue;
            }

            // 修改分隔符为：\t#，即去除空格 2018-06-01 10:22:07
            String[] data = Utils.tokenizeToStringArray(line, "\t#"); // <Tab>#
            if (data.length >= 3) {
                String text = data[0]; // 言
                String code = data[1]; // yyyy
                String weight = data[2]; // 序15944

                weight = weight.replace("序", "");

                addEntryToList(outList, code, text, weight, type, formatType);
            } else if (data.length == 2) {
                String text = data[0]; // 言
                String code = data[1]; // yyyy

                addEntryToList(outList, code, text, "0", type, formatType);
            } else if (data.length == 1) {
                // 若只有一个元素，则只能是汉字 2018-06-16 09:55:11
                String text = data[0]; // 言

                addEntryToList(outList, "--", text, "0", type, formatType);
            }
        }
    }

    /**
     * 加载输入法词条数据（使用 多多输入法原始码表格式 保存）
     *
     * @param filePath 文件路徑
     * @param outList List
     * @throws IOException 读/写文件错误
     * @see #loadImeDictData(String, java.util.List, FormatType)
     * @since 2018-05-25 14:01
     */
    protected void loadImeDictData(Path filePath, List<ImeEntry> outList) throws IOException {
        String filename = filePath.getFileName().toString();
        List<String> list = Files.readAllLines(filePath);
        System.out.println("行数：" + list.size() + "\t\t" + filename);

        String type = FILE_TYPE.get(filename);
        parseEntry(list, type, outList, FormatType.DuoDuo);
    }

    /**
     * Add wubi/zhengma entry to list
     *
     * @param list List
     * @param code 编码
     * @param text 词条
     * @param weight 词频
     * @param type 分类
     * @param formatType 输出数据的格式
     * @version 2018-03-20 13:33:40 添加一个参数：boolean asMySQLFormat
     * @since 2018-04-21 17:33 从 inputmethod.wubi4me.Test01 类中移过来的
     */
    public static void addEntryToList(List<ImeEntry> list, String code, String text, String weight, String type, FormatType formatType) {
        ImeEntry entry = new ImeEntry(code, text);
        if (FormatType.MySQL == formatType) {
            entry = new MySQLLoadEntry(code, text, weight, type, null);
        } else if (FormatType.DuoDuo == formatType) {
            entry = new DDImeEntry(code, text, weight, type);
        } else if (FormatType.Rime == formatType) {
            entry = new RimeEntry(code, text, weight, type);
        } else if (FormatType.SQL_INSERT == formatType) {
            entry = new SQLInsertEntry(code, text, weight, type);
        } else if (FormatType.SQL_UPDATE == formatType) {
            entry = new SQLUpdateEntry(code, text, weight, type, null);
        }
        list.add(entry);
    }

    /**
     * 解析詞庫
     *
     * @param srcFiles 要解析的詞庫的文件名稱
     * @param entryCount 詞條總數
     * @param outFilename 輸出文件按名稱
     * @throws java.io.IOException 读/写文件错误
     * @since 2018-05-17 12:55
     */
    protected void analyzeDict(String[] srcFiles, int entryCount, String outFilename) throws IOException {
        List<ImeEntry> list = new ArrayList<>(entryCount);

        for (String file : srcFiles) {
            loadImeDictData(file, list, FormatType.MySQL);
        }

        int size = list.size();
        System.out.println("词条总数：" + size);

        // 輸出之前，去除重複的詞條 2018-05-17 21:45:35
        list = removeDuplicateEntry(list);

        Path dstPath = Paths.get(OUT_PATH, outFilename);
        Files.write(dstPath, list);
        System.out.println(dstPath);
    }

    /**
     * 去除重复的数据
     *
     * @param list List
     * @return List
     * @version 2018-05-29 16:11 return List<ImeEntry>
     * @since 2018-05-17 21:47
     */
    private List<ImeEntry> removeDuplicateEntry(List<ImeEntry> list) {
        // 重复数据 2018-03-27 21:28:06
        List<ImeEntry> duplicatedList = new ArrayList<>(20);

        // 使用 HashSet 判断重复，可以通过 new ArrayList(set) 转换为 List 2018-03-27 21:28:56
        HashSet<ImeEntry> entrySet = new HashSet<>(list.size());
        for (ImeEntry entry : list) {
            boolean added = entrySet.add(entry);
            // 如果返回 false，则表示要添加的对象已存在
            if (!added) {
                // System.out.println("Hash: " + entry.hashCode());
                duplicatedList.add(entry);
            }
        }

        // 打印重复的数据
        if (!duplicatedList.isEmpty()) {
            System.out.println("------------------------------");
            for (ImeEntry entry : duplicatedList) {
                System.out.println("重复词条：" + entry);
            }
            System.out.println("重复词条的行数：" + duplicatedList.size());
            System.out.println("------------------------------");
        }

        // 移除重复的数据 2018-05-29 16:09:22
        list = new ArrayList<>(entrySet);
        return list;
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
     * @param filename 要處理的文件的名稱，在 {@value #dictPath} 路徑下
     * @param resetWeight 是否重新設置排序編號（詞頻）
     * @param defaultWeight 默认词频
     * @throws IOException 读/写文件错误
     * @version 2017-08-23 10:38:53 用来处理拼音词库
     * @version 2018-03-20 11:17:30 从已删除的 Test02.java 类移过来
     * @version 2018-03-20 13:41:42 修改 buildMySQLLoadData() 方法的签名及定义，以适用于该方法
     * @version 2018-07-06 09:29 添加参数：defaultWeight
     * @see DDImeEntry#toString()
     * @see ImeEntry#equals(Object)
     * @since 2018-05-17 21:30
     */
    protected void reArrangeDict(String filename, boolean resetWeight, String defaultWeight) throws IOException {
        List<ImeEntry> list = new ArrayList<>(355457 + 10);

        loadImeDictData(filename, list, FormatType.DuoDuo); // 输出为多多格式 2018-03-20 13:37:20

        int size = list.size();
        System.out.println("词条总数：" + size);

        // 对词条进行分组，以编码（五笔代码、拼音）为组名，内容为编码对应的词条
        // 对分组的词条，按编码升序排序 2018-03-22 10:24:34
        Map<String, List<ImeEntry>> codeMap = new TreeMap<>(String::compareTo);
        for (ImeEntry entry : list) {
            String code = entry.getCode();
            List<ImeEntry> entryList = codeMap.get(code);
            if (entryList == null) {
                // 词库中共有 157395 条拼音，其中 yi 编码的词条数最多，共 469 条
                entryList = new ArrayList<>(469);
                entryList.add(entry);
                codeMap.put(code, entryList);
            } else {
                // 如果 entryList 中已存在该词条，则取出来，将排序编号相加，并设置为新序号。
                // （需要重写 ImeEntry#equals(Object) 方法）2018-03-22 11:26:13
                int idx = entryList.indexOf(entry);
                if (idx >= 0) {
                    System.out.println("已存在（" + entry + "）");
                    ImeEntry existedEntry = entryList.get(idx);
                    // int i1 = Utils.safeToInt(existedEntry.getWeight());
                    // int i2 = Utils.safeToInt(entry.getWeight());
                    // String newWeight = String.valueOf(i1 + i2);
                    String newWeight = Utils.calcAverageValue(existedEntry.getWeight(), entry.getWeight());
                    existedEntry.setWeight(newWeight);
                } else {
                    entryList.add(entry);
                }
            }
        }
        System.out.println("已分组，共：" + codeMap.size() + " 组");

        // 对每个组的数据进行排序 2017-08-23 14:17:08
        codeMap.forEach((code, entryList) -> sortByWeight(entryList));
        System.out.println("已对每组词条排序");

        // 对已排序的词条重新设置编号 2017-08-29 17:50:25
        if (resetWeight) {
            // 不再生成编号，改为使用下载的词频：词频-海峰.txt 2018-05-28 17:53:19
            Map<String, String> weightMap = loadEntryWeights();
            codeMap.forEach((code, entryList) -> resetWeight(entryList, weightMap, defaultWeight));
            System.out.println("已重新设置词频");
        }

        // 再将所有组的数据合成一个 List
        List<ImeEntry> outList = new ArrayList<>(355457 + 10);
        codeMap.forEach((code, entryList) -> outList.addAll(entryList));
        System.out.println("已合并");

        // 对合并后的词条排序 2018-07-05 10:28:14
        sortByCodeAndWeight(outList);
        System.out.println("已对合并后的词条排序");

        // 将 List 的数据写入到文件中
        Path dstPath = Paths.get(OUT_PATH, filename);
        Files.write(dstPath, outList);
        System.out.println("已写入文件：\n" + dstPath);
    }

    /**
     * 词条排序（基于词频）
     * <p>根据词条的词频排序：词频越大，越靠前；词频相等（很多1024），位置不变。</p>
     *
     * @param list 词条列表
     * @since 2017-08-23 14:27
     */
    private void sortByWeight(List<ImeEntry> list) {
        // if (list==null||list.isEmpty()) {return;}
        list.sort((o1, o2) -> {
            String weight1 = o1.getWeight();
            String weight2 = o2.getWeight();
            // 基于词频，降序排序
            return Integer.compare(Utils.safeToInt(weight2), Utils.safeToInt(weight1));
        });
    }

    /**
     * 词条排序（基于编码、词频）
     *
     * <p>词条排序：先根据编码（字母）排序，再根据词频排序。</p>
     *
     * @param list 词条列表
     * @since 2018-07-05 10:32
     */
    private void sortByCodeAndWeight(List<ImeEntry> list) {
        // if (list==null||list.isEmpty()) {return;}
        list.sort((o1, o2) -> {
            String code1 = o1.getCode();
            String code2 = o2.getCode();
            // 先按编码升序排序
            int result = code1.compareTo(code2);
            if (result == 0) {
                String weight1 = o1.getWeight();
                String weight2 = o2.getWeight();
                // 再按词频降序排序
                result = Integer.compare(Utils.safeToInt(weight2), Utils.safeToInt(weight1));
                if (result == 0) {
                    // 再按词条的ASCII码排序 2018-08-24 17:57
                    String text1 = o1.getText();
                    String text2 = o2.getText();
                    return text1.compareTo(text2);
                }
                return result;
            } else {
                return result;
            }
        });
    }

    /**
     * 加载词频文件
     *
     * @since 2018-04-25 18:03:44
     */
    private static Map<String, String> loadEntryWeights() throws IOException {
        Map<String, String> entryWeightMap = new HashMap<>(139953 + 10);

        // 现代汉语语料库词语频率表 下载自 www.cncorpus.org 语料库在线网站 2018-05-30 20:50:42
        // 语料规模：2000万字，只列入出现次数大于50次的词
        Path filePath = Paths.get(dictPath, "../../汉字相关/词频-语料库.txt");
        List<String> list = Files.readAllLines(filePath);

        for (String line : list) { // 的	744863
            String[] arr = line.split("\t");
            String text = arr[0];
            String weight = arr[1];
            entryWeightMap.put(text, weight);
        }
        return entryWeightMap;
    }

    /**
     * 设置 {@link ImeEntry#weight 词频}
     *
     * @param list List
     * @param weightMap 词频列表
     * @param defaultWeight 默认词频
     * @version 2018-05-28 18:20 改为使用海峰词频
     * @version 2018-05-30 20:56 改为使用 “词频-语料库.txt”
     * @since 2018-07-06 09:28 添加参数：defaultWeight
     * @since 2017-08-29 17:56
     */
    private void resetWeight(List<ImeEntry> list, Map<String, String> weightMap, String defaultWeight) {
        for (ImeEntry entry : list) {
            String text = entry.getText();

            // 从 词频列表 中获取词条的词频；不存在的词条，设置词频为10 2018-05-28 18:17:09
            // 使用 语料库词频：该词频中，最小词频为 51；不存在的字使用默认词频，常用字的默认词频为 50，词组 45，
            // 生僻字（CJK-A,B,C,D）10，E、F及兼容区为 0，不是 CJK-Unified 的字为 -1 2019-08-25 22:41:40
            String weight = weightMap.get(text);
            if (weight == null) {
                // 如果词频表中不存在这个字，先判断该字是不是 CJK-Unified 字，如果是，则使用 defaultWeight，否则 -1
                weight = CodePointUtil.isCJKUnifiedIdeographicChar(text) ? defaultWeight : "-1";
                if (isCJKEFCompChar(text)) {
                    weight = "0";
                }
            }
            entry.setWeight(weight);
        }
    }

    /**
     * 是否是 CJK E、F及兼容区字
     *
     * @since 2019-08-26 17:08
     */
    private boolean isCJKEFCompChar(String ch) {
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
     * @param entryList 词条列表，不能为空
     * @return 包含 词条-编码 对应关系的Map
     * @since 2018-08-27 14:12
     */
    protected Map<String, List<String>> convertEntryListToTextMap(List<ImeEntry> entryList) {
        //if (entryList == null || entryList.isEmpty()) {return Collections.emptyMap();}

        // 构建 词条-编码 对应关系
        Map<String, List<String>> textMap = new HashMap<>(entryList.size() - 200);
        for (ImeEntry entry : entryList) {
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
                    System.out.println("重复词条：" + entry);
                } else {
                    codeList.add(code);
                }
            }
        }
        return textMap;
    }

}
