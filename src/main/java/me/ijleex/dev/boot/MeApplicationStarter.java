/*
 * Copyright 2011-2022 the original author or authors.
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

    public MeApplicationStarter() {
    }

    /**
     * 启动
     *
     * @param args 参数
     * @see SpringApplication#run(Class, String...)
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MeApplicationStarter.class);
        app.run(args);
    }

    static {
        final String path = MeApplicationStarter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.endsWith("!/BOOT-INF/classes!/")) { // Spring-Boot Fatjar
            File file = new File("");
            System.setProperty("app.path", file.getAbsolutePath());
        } else {
            File file = new File(path);
            System.setProperty("app.path", file.getParent());
        }

        // 禁用重新启动（RestartClassLoader）2018-11-06 15:03
        System.setProperty("spring.devtools.restart.enabled", "false");
    }
}
