/*
 * Copyright 2011-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.boot.action;

import java.util.Arrays;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample action.
 *
 * @author liym
 * @since 2018-04-11 17:00 新建
 */
@RequestMapping("/me")
@RestController
public final class MeAction {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用于获取当前 Spring-Boot 环境的配置信息
     *
     * @see org.springframework.web.context.support.StandardServletEnvironment
     * @since 2018-04-12 12:39
     */
    private final Environment env;

    public MeAction(Environment env) {
        this.env = env;
    }

    /**
     * Hello Spring-Boot
     *
     * @param request 当前请求
     * @return msg
     */
    @RequestMapping(path = "/hello", method = RequestMethod.GET)
    public String hello(HttpServletRequest request) {
        logger.debug("Hello request: {}", request);
        String version = SpringBootVersion.getVersion();
        return "Hello Spring-Boot v" + version;
    }

    /**
     * 获取当前 Spring-Boot 运行环境的配置文件
     *
     * <pre> {@code
     * @Value("${spring.profiles.active}")
     * private String activeProfile;
     *
     * @Value("${spring.profiles.include}")
     * private String includeProfile;
     * }
     * </pre>
     *
     * @param request 当前请求
     * @return application.yml 中 spring.profiles.active 对应的值，没有设置，则返回 default
     * @since 2018-04-12 12:36
     */
    @RequestMapping(path = "/profiles", method = RequestMethod.GET)
    public String getProfiles(HttpServletRequest request) {
        /// logger.debug("getProfiles via Environment: {}", this.env);
        String myProfile = "default";
        String[] activeProfiles = this.env.getActiveProfiles();
        if (activeProfiles.length > 0) {
            myProfile = Arrays.toString(activeProfiles);
        }
        logger.debug("getProfiles: {}", myProfile);
        return myProfile;
    }

}
