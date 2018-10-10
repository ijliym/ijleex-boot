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
 * @author ijymLee
 * @since 2018-04-18 13:22 新建：想学习郑码
 */
package me.ijleex.dev.test.inputmethod;
