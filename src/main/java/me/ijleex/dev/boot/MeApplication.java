/*
 * Copyright 2011-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.boot;

import java.io.File;
import java.security.ProtectionDomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring-Boot 启动程序
 *
 * @author liym
 * @since 2018-04-10 18:14 新建
 */
@SpringBootApplication
public class MeApplication {

    public MeApplication() {
    }

    /**
     * 启动
     *
     * @param args 参数
     * @see SpringApplication#run(Class, String...)
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MeApplication.class);
        app.run(args);
    }

    static {
        ProtectionDomain protectionDomain = MeApplication.class.getProtectionDomain();
        String path = protectionDomain.getCodeSource().getLocation().getPath();
        if (path.startsWith("nested:") && path.endsWith("/!BOOT-INF/classes/!/")) { // Spring-Boot FatJar
            File file = getFatJarFile(path);
            if (file != null) {
                path = file.getParent();
            } else {
                path = new File("").getAbsolutePath();
            }
            System.err.println("app.path=" + path);
            System.setProperty("app.path", path);
        } else { // IDE startup
            File file = new File(path);
            System.err.println("app.path=" + file.getParent());
            System.setProperty("app.path", file.getParent());
        }

        // 禁用重新启动（RestartClassLoader）2018-11-06 15:03
        System.setProperty("spring.devtools.restart.enabled", "false");
    }

    /**
     * 获取启动Spring-Boot服务的FatJar文件.
     *
     * @param path {@link java.security.CodeSource}类加载器加载的代码来源路径
     * @return Spring-Boot FatJar 文件
     * @author liym
     * @since 2025-12-27 22:25:27
     */
    private static File getFatJarFile(String path) {
        // nested:/path/to/spring-boot-x.x.x-exec.jar/!BOOT-INF/classes/!/
        path = path.replaceAll("nested:", "").replaceAll("/!BOOT-INF/classes/!/", "");
        File file = new File(path);
        return (file.canRead() && file.isFile()) ? file : null;
    }

}
