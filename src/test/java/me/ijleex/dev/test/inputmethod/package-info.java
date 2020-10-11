/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

/**
 * 输入法词库处理
 *
 * <p>处理的词库不仅可以用于多多输入法生成器，也可以用于 Rime 输入法。所以，计划将 “duouoime” 包下的内容移到这里，
 * 并添加处理郑码词库的功能。</p>
 *
 * <p>因为好几个字的拆法，如 谶、栽 等，尤其是最近两天遇到的两个字：凢、凣，我认为是上下结构的，但是五笔的编码却
 * 分别为：twv、hwv，使我十分地不爽！还有五笔把字拆的太散了，所以想研究一下郑码的 “大字根”。</p>
 *
 * <p>Unicode blocks: https://unicode-table.com/en/blocks/</p>
 *
 * <p><a href="https://en.wikipedia.org/wiki/Chinese_input_methods_for_computers"> wiki "Chinese
 * input methods for computers"</a></p>
 *
 * <p><a href="http://www.aihanyu.org/cncorpus/resources.aspx">语料库 研究资源下载</a></p>
 *
 * @author liym
 * @since 2018-04-18 13:22 新建：想学习郑码
 */
package me.ijleex.dev.test.inputmethod;
