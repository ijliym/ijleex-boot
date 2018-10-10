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
 * @author ijymLee
 * @since 2018-04-10 18:14 新建
 */
@SpringBootApplication
public class MeApplicationStarter {

    /**
     * 启动
     *
     * @param args 参数
     * @see SpringApplication#run(Class, String...)
     * @see org.springframework.context.support.LiveBeansView
     */
    public static void main(String[] args) {
        // 与 spring.jmx.default-domain 的值相同：org.springframework.boot 2018-04-19 10:26:25
        System.setProperty("spring.liveBeansView.mbeanDomain", "org.springframework.boot");

        SpringApplication app = new SpringApplication(MeApplicationStarter.class);
        app.run(args);
    }

}
