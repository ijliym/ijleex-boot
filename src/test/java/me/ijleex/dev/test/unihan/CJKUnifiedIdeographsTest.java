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

package me.ijleex.dev.test.unihan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.text.UnicodeSet;
import org.junit.Test;

/**
 * CJK Unified Ideographs (https://en.wikipedia.org/wiki/CJK_Unified_Ideographs).
 *
 * <p>The Chinese, Japanese and Korean (CJK) scripts share a common background, collectively known as
 * <a href="https://en.wikipedia.org/wiki/CJK_characters">CJK characters</a>.
 * In the process called <a href="https://en.wikipedia.org/wiki/Han_unification">Han unification</a>,
 * the common (shared) characters were identified and named "CJK Unified Ideographs."
 * As of Unicode 11.0, Unicode defines a total of <b>87,887</b> CJK Unified Ideographs.</p>
 *
 * <p>The terms ideographs or ideograms may be misleading, since the Chinese script is not strictly a picture writing system.</p>
 *
 * <p>Historically, Vietnam used Chinese ideographs too, so sometimes the abbreviation "CJKV" is used.
 * This system was replaced by the Latin-based Vietnamese alphabet in the 1920s.</p>
 *
 * @author liym
 * @since 2017-09-07 10:06 新建
 */
public class CJKUnifiedIdeographsTest {

    private static final String PATH = "D:/ProgramFiles/MySQL/mysql-8.0.15-winx64/run/";

    /**
     * CJK Unified Ideographs (中日韩统一表意文字)
     *
     * <p>The basic block named CJK Unified Ideographs (4E00–9FFF) contains 20,976 basic Chinese characters in
     * the range U+4E00 through U+9FEF.</p>
     *
     * @throws java.io.IOException 写文件异常
     * @since 2017-09-07 10:07
     */
    @Test
    public void test01() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x4E00, 0x9FEF);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-unified-ideographs";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * 提取 {@link com.ibm.icu.text.UnicodeSet} 字符集包含的字符到 List 中
     *
     * @param unicodeSet Unicode 字符集
     * @param unicodeBlock Unicode 字符集所属名称
     * @return List
     * @since 2017-09-08 13:28
     */
    private List<UnihanEntry> extractUnicodeSet(UnicodeSet unicodeSet, String unicodeBlock) {
        int size = unicodeSet.size();
        List<UnihanEntry> list = new ArrayList<>(size);

        unicodeSet.forEach(hanChar -> addTo(list, hanChar, unicodeBlock));

        return list;
    }

    /**
     * 将 UnicodeSet 中提取出来的字符添加到 List 中
     *
     * @param list List
     * @param hanChar 汉字
     * @param unicodeBlock Unicode中所属块
     * @since 2017-09-07 11:25
     */
    private void addTo(List<UnihanEntry> list, String hanChar, String unicodeBlock) {
        int codePoint = CodePointUtil.getCodePoint(hanChar);
        String codeHex = CodePointUtil.intAsHex(codePoint);

        UnihanEntry entry = new UnihanEntry(hanChar, codePoint, codeHex, unicodeBlock);
        list.add(entry);
    }

    /**
     * 将 List 中包含的字符写入到文件
     *
     * @param list List
     * @param filename 文件名
     * @throws IOException 写文件异常
     * @since 2017-09-08 13:32
     */
    private void writeToFile(List<UnihanEntry> list, String filename) throws IOException {
        // FileUtils.writeLines(outFile, "UTF-8", list, "\n");

        // 设置分行符（为 UNIX）2018-03-20 14:50:54
        System.setProperty("line.separator", "\n");

        Path dstPath = Paths.get(PATH, filename + ".txt");
        Files.write(dstPath, list);
        System.out.println(dstPath);
    }

    /**
     * CJK Unified Ideographs Extension A (中日韩统一表意文字扩展A)
     *
     * <p>The block named CJK Unified Ideographs Extension A (3400–4DBF) contains 6,582 additional characters
     * in the range U+3400 through U+4DB5 that were added in Unicode 3.0 (1999).</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-07 19:34
     */
    @Test
    public void test02() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x3400, 0x4DB5);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-unified-ideographs-extension-a";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Unified Ideographs Extension B
     *
     * <p>The block named CJK Unified Ideographs Extension B (20000–2A6DF) contains 42,711 characters in the
     * range U+20000 through U+2A6D6 that were added in Unicode 3.1 (2001). These include most of the characters
     * used in the Kangxi Dictionary that are not in the basic CJK Unified Ideographs block,
     * as well as many Nôm characters that were formerly used to write Vietnamese.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-07 19:36
     */
    @Test
    public void test03() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x20000, 0x2A6D6);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-unified-ideographs-extension-b";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Unified Ideographs Extension C
     *
     * <p>The block named CJK Unified Ideographs Extension C (2A700–2B73F) contains 4,149 characters in
     * the range U+2A700 through U+2B734 that were added in Unicode 5.2 (2009).</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-07 19:38
     */
    @Test
    public void test04() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2A700, 0x2B734);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-unified-ideographs-extension-c";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Unified Ideographs Extension D
     *
     * <p>The block named CJK Unified Ideographs Extension D (2B740–2B81F) contains 222 characters in
     * the range U+2B740 through U+2B81D that were added in Unicode 6.0 (2010).</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-07 19:39
     */
    @Test
    public void test05() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2B740, 0x2B81D);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-unified-ideographs-extension-d";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Unified Ideographs Extension E
     *
     * <p>The block named CJK Unified Ideographs Extension E (2B820–2CEAF) contains 5,762 characters in
     * the range U+2B820 through U+2CEA1 that were added in Unicode 8.0 (2015).</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-07 19:40
     */
    @Test
    public void test06() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2B820, 0x2CEA1);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-unified-ideographs-extension-e";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Unified Ideographs Extension F
     *
     * <p>The block named CJK Unified Ideographs Extension F (2CEB0–2EBEF) contains 7,473 characters in
     * the range U+2CEB0 through 2EBE0 that were added in Unicode 10.0 (2017).
     * It includes more than 1,000 Sawndip characters for Zhuang.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-07 19:41
     */
    @Test
    public void test07() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2CEB0, 0x2EBE0);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-unified-ideographs-extension-f";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Compatibility Ideographs (中日韩兼容表意文字)
     *
     * <p>The block named CJK Compatibility Ideographs (F900–FAFF) was created to retain round-trip
     * compatibility with other standards. Only twelve of its characters have the "Unified Ideograph"
     * property: U+FA0E, FA0F, FA11, FA13, FA14, FA1F, FA21, FA23, FA24, FA27, FA28 and FA29.
     * None of the other characters in this and other "Compatibility" blocks relate to CJK Unification.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-07 19:42
     */
    @Test
    public void test08() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet();
        unicodeSet.add(0xFA0E);
        unicodeSet.add(0xFA0F);
        unicodeSet.add(0xFA11);
        unicodeSet.add(0xFA13);
        unicodeSet.add(0xFA14);
        unicodeSet.add(0xFA1F);
        unicodeSet.add(0xFA21);
        unicodeSet.add(0xFA23);
        unicodeSet.add(0xFA24);
        unicodeSet.add(0xFA27);
        unicodeSet.add(0xFA28);
        unicodeSet.add(0xFA29);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-compatibility-ideographs";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Radicals Supplement (中日韩部首补充)
     *
     * <p>CJK Radicals Supplement is a Unicode block containing alternative, often positional,
     * forms of the Kangxi radicals. They are used headers in dictionary indices and other
     * CJK ideograph collections organized by radical-stroke.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 17:22
     */
    @Test
    public void test09() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2E80, 0x2EF3); // 0x2EFF
        unicodeSet.remove(0x2E9A); // 空闲 2018-05-06 17:03:27

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-radicals-supplement";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Strokes (中日韩笔画)
     *
     * <p>http://www.unicode.org/charts/PDF/U31C0.pdf</p>
     *
     * <p>CJK Strokes is a Unicode block containing examples of each of the standard CJK stroke types.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 17:39
     */
    @Test
    public void test10() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x31C0, 0x31E3); // 0x31EF

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-strokes";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Symbols and Punctuation (中日韩符号和标点)
     *
     * <p>CJK Symbols and Punctuation is a Unicode block containing symbols and punctuation in the unified Chinese,
     * Japanese and Korean script.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 17:42
     */
    @Test
    public void test11() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x3000, 0x303F);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-symbols-and-punctuation";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * Ideographic Description Characters (表意文字描述符)
     *
     * <p>Ideographic Description Characters is a Unicode block containing graphic characters used
     * for describing CJK ideographs.
     * They are not intended to provide a mechanism for the composition of complex characters,
     * whether already encoded or not, but are used within
     * Ideographic Description Sequences (IDS) for that purpose.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 17:47
     */
    @Test
    public void test12() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2FF0, 0x2FFB); // 0x2FFF

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "ideographic-description-characters";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Compatibility (中日韩字符集兼容)
     *
     * <p>CJK Compatibility is a Unicode block containing square symbols (both CJK and Latin alphanumeric)
     * encoded for compatibility with east Asian character sets.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 17:53
     */
    @Test
    public void test13() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x3300, 0x33FF);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-compatibility";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Compatibility Forms (中日韩兼容形式)
     *
     * <p>CJK Compatibility Forms is a Unicode block containing vertical glyph variants for east Asian compatibility.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 17:55
     */
    @Test
    public void test14() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0xFE30, 0xFE4F);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "cjk-compatibility-forms";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * CJK Compatibility Ideographs Supplement (中日韩兼容表意文字补充)
     *
     * <p><b>Not unified</b></p>
     *
     * <p>https://en.wikipedia.org/wiki/CJK_Compatibility_Ideographs_Supplement</p>
     *
     * <p>CJK Compatibility Ideographs Supplement is a Unicode block containing Han characters used
     * only for roundtrip compatibility mapping with planes 3, 4, 5, 6, 7, and 15 of CNS 11643-1992.</p>
     *
     * <p><b> 2F800:2FA1F</b></p>
     *
     * @throws IOException 写文件异常
     * @since 2017-09-14 13:18
     */
    @Test
    public void test15() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2F800, 0x2FA1D); // 0x2FA1F

        int size = unicodeSet.size();
        System.out.println("字数：" + size); // 542

        String blockId = "cjk-compatibility-ideographs-supplement";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * Enclosed CJK Letters and Months (带圈中日韩字母和月份)
     *
     * <p>Enclosed CJK Letters and Months is a Unicode block containing circled and parenthesized
     * Katakana, Hangul, and CJK ideographs. During the unification with ISO 10646 for version 1.1,
     * the Japanese Industrial Standard Symbol was reassigned from the code point U+32FF at the end of
     * the block to U+3004. Also included in the block are miscellaneous glyphs that would more likely
     * fit in CJK Compatibility or Enclosed Alphanumerics: a few unit abbreviations,
     * circled numbers from 21 to 50, and circled multiples of 10 from 10 to 80 enclosed in
     * black squares (representing speed limit signs).</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 18:07
     */
    @Test
    public void test16() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x3200, 0x32FF); // 0x2FA1F
        unicodeSet.remove(0x321E); // Reserved (保留)
        unicodeSet.remove(0x32FF); // Reserved

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "enclosed-cjk-letters-and-months";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * <a href="https://en.wikipedia.org/wiki/Enclosed_Ideographic_Supplement">
     * Enclosed Ideographic Supplement </a> (带圈表意字补充)
     *
     * <p>http://www.unicode.org/charts/PDF/U1F200.pdf</p>
     *
     * <p>Enclosed Ideographic Supplement is a Unicode block containing characters for compatibility with
     * the Japanese ARIB STD-B24 standard. It contains a squared kana word,
     * and many CJK ideographs enclosed with squares, brackets, or circles.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 18:11
     */
    @Test
    public void test17() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(); // 0x1F200, 0x2FA1F
        unicodeSet.add(0x1F200);          // Squared hiragana from ARIB STD B24
        unicodeSet.add(0x1F201, 0x1F202); // Squared katakana
        unicodeSet.add(0x1F210, 0x1F21F); // Squared ideographs and kana from ARIB STD B24
        unicodeSet.add(0x1F220, 0x1F22F);
        unicodeSet.add(0x1F230, 0x1F23B);
        unicodeSet.add(0x1F240, 0x1F248);
        unicodeSet.add(0x1F250, 0x1F251);
        unicodeSet.add(0x1F260, 0x1F265); // Symbols for Chinese folk religion

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "enclosed-ideographic-supplement";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * <a href="https://en.wikipedia.org/wiki/Kangxi_radical#Unicode">Kangxi radical</a> (康熙部首)
     *
     * <p>
     * <a href="https://upload.wikimedia.org/wikipedia/commons/3/3b/List_of_the_214_Kangxi_Radicals_-_old_style.svg">
     * List of Kangxi radicals</a>
     * </p>
     *
     * <p>The 214 Kangxi radicals (康熙部首) form a system of radicals (部首) of Chinese characters.
     * The radicals are numbered in stroke count order.
     * They are the de facto standard used as the basis for most modern Chinese dictionaries,
     * such that reference to "radical 61", for example, without additional context,
     * refers to the 61st radical of the Kangxi Dictionary, 心; xīn "heart".</p>
     * <p>Originally introduced in the 1615 Zihui, they are named in relation to the Kangxi Dictionary of 1716
     * (Kāngxī 康熙 being the era name for 1662–1723). The system of 214 Kangxi radicals is based on the
     * older system of 540 radicals used in the Han-era
     * <a href="https://en.wikipedia.org/wiki/Shuowen_Jiezi">Shuowen Jiezi</a>.</p>
     *
     * @throws IOException 写文件异常
     * @since 2017-10-15 18:20
     */
    @Test
    public void test18() throws IOException {
        UnicodeSet unicodeSet = new UnicodeSet(0x2F00, 0x2FD5); //  0x2FDF

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        String blockId = "kangxi-radicals";

        List<UnihanEntry> list = extractUnicodeSet(unicodeSet, blockId);
        writeToFile(list, blockId);
    }

    /**
     * @since 2017-09-15 17:25
     */
    @Test
    public void test19() {
        UnicodeSet unicodeSet = new UnicodeSet();
        unicodeSet.add(0x1F22E);

        int size = unicodeSet.size();
        System.out.println("字数：" + size);

        unicodeSet.forEach(System.out::println);
    }

    /**
     * 判断字符是否 CJK 统一汉字
     *
     * @since 2017-09-21 16:29:41
     */
    @Test
    public void test20() {
        System.out.println(CodePointUtil.isCJKUnifiedIdeographicChar("火"));
        System.out.println(CodePointUtil.isCJKUnifiedIdeographicChar("𤳳"));
        System.out.println(CodePointUtil.isCJKUnifiedIdeographicChar("©"));
        System.out.println(CodePointUtil.isCJKUnifiedIdeographicChar("〇"));
    }

}
