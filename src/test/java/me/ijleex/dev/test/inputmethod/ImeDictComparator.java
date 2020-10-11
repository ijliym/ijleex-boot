/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test.inputmethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;

import com.ibm.icu.text.UnicodeSet;

import me.ijleex.dev.test.unihan.CodePointUtil;

/**
 * 用于比较郑码、五笔单字与 CJK 统一表意文字之间的差异
 *
 * @author liym
 * @since 2019-08-25 20:38 新建
 */
public class ImeDictComparator {

    /**
     * 找出郑码单字中不是 CJK 统一表意文字的字
     *
     * @since 2019-08-25 20:46
     */
    @Test
    public void testFigureOutNotCJKUnified01() throws IOException {
        // 郑码单字，01.郑码-主码-常用字.txt + 05.郑码-次显-生僻字.txt 中的单字
        String charPath = "C:/Users/me/Documents/InputMethod/[超集郑码]/原始码表/郑码单字.txt";
        Path path = Paths.get(charPath);

        List<String> chars = Files.readAllLines(path);
        chars.forEach(ch -> {
            if (!CodePointUtil.isCJKUnifiedIdeographicChar(ch)) {
                System.out.println("> " + ch);
            }
        });
    }

    /**
     * 找出 CJK 统一表意文字 在郑码单字中不存在的字
     *
     * @since 2019-08-25 21:26
     */
    @Test
    public void testFigureOutCJKUnifiedNotInDict02() throws IOException {
        // 郑码单字，01.郑码-主码-常用字.txt + 05.郑码-次显-生僻字.txt 中的单字
        String charPath = "C:/Users/me/Documents/InputMethod/[超集郑码]/原始码表/郑码单字.txt";
        Path path = Paths.get(charPath);

        UnicodeSet dictSet = new UnicodeSet();

        List<String> chars = Files.readAllLines(path);
        chars.forEach(dictSet::add);

        UnicodeSet cjkSet = CodePointUtil.buildCJKUnifiedIdeographsSet();
        cjkSet.forEach(ch -> {
            if (!dictSet.contains(ch)) {
                System.out.println("> " + ch);
            }
        });
    }

}
