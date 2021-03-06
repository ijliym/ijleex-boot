/*
 * Copyright 2011-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.boot;

import java.io.File;

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

    static {
        String path = MeApplicationStarter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(path);
        System.setProperty("app.path", file.getParent());
    }

    public MeApplicationStarter() {}

    /**
     * 启动
     *
     * @param args 参数
     * @version 2018-11-06 15:03 删除属性值：spring.liveBeansView.mbeanDomain，添加：spring.devtools.restart.enabled=false
     * @see SpringApplication#run(Class, String...)
     */
    public static void main(String[] args) {
        // 禁用重新启动（RestartClassLoader）2018-11-06 15:03
        System.setProperty("spring.devtools.restart.enabled", "false");

        SpringApplication.run(MeApplicationStarter.class, args);
    }

}
