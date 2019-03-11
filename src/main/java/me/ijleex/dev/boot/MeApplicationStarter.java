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

package me.ijleex.dev.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring-Boot 启动程序
 *
 * @author liym
 * @since 2018-04-10 18:14 新建
 */
@SpringBootApplication
public class MeApplicationStarter {

    /**
     * 启动
     *
     * @param args 参数
     * @version 2018-11-06 15:03 删除属性值：spring.liveBeansView.mbeanDomain，添加：spring.devtools.restart.enabled=false
     * @see SpringApplication#run(Class, String...)
     */
    public static void main(String[] args) {
        // 禁用重新启动（RestartClassLoader）2016-11-06 15:03
        System.setProperty("spring.devtools.restart.enabled", "false");

        SpringApplication.run(MeApplicationStarter.class, args);
    }

}
