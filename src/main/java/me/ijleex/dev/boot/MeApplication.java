/*
 * Copyright 2011-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.boot;

import java.io.File;
import java.io.FilePermission;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.util.Enumeration;

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
        if (path.endsWith("/!BOOT-INF/classes/!/")) { // Spring-Boot FatJar
            File file = getFatJarFile(protectionDomain);
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
     * 根据{@link ProtectionDomain}获取启动Spring-Boot服务的FatJar文件.
     *
     * <p>当使用Linux系统 {@code cron} 服务启动 Spring-Boot 项目时，{@code path = new File("").getAbsolutePath()}
     * 会获取到当前用户的根目录路径（/home/xxx），致使依赖 {@code ${app.path}/log} 的日志目录会出现在用户根目录下，而不是项目目录下。</p>
     *
     * @param protectionDomain {@link ProtectionDomain}
     * @return Spring-Boot FatJar 文件
     * @author liym
     * @since 2023-02-17 11:08
     */
    private static File getFatJarFile(ProtectionDomain protectionDomain) {
        PermissionCollection permissions = protectionDomain.getPermissions();
        Enumeration<Permission> elements = permissions.elements();
        File file = null;
        while (elements.hasMoreElements()) {
            Permission permission = elements.nextElement();
            if (permission instanceof FilePermission) {
                String fileName = permission.getName();
                file = new File(fileName);
                if (file.canRead()) {
                    break;
                }
            }
        }
        return file;
    }

}
