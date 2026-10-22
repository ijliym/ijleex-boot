/*
 * Copyright © 2011-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 计算 SSH（RSA/ED25519）公钥的 SHA256 指纹.
 *
 * @author liym
 * @since 2026-10-22 14:06 新建
 */
public class SSHFingerprintTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SSHFingerprintTest() {
    }

    /**
     * 计算 SSH（RSA/ED25519）公钥的 SHA256 指纹.
     *
     * <p>
     * <pre>
     * {@code
     * ssh-keygen -l -E SHA256 -f ~/.ssh/id_rsa.pub
     * ssh-keygen -l -E SHA256 -f ~/.ssh/id_ed25519.pub
     * }
     * </pre>
     * </p>
     *
     * @throws IOException 输入输出异常
     * @throws NoSuchAlgorithmException 算法异常
     */
    @Test
    public void fingerprintHash() throws IOException, NoSuchAlgorithmException {
        Path file = Paths.get(this.userHome(), "/.ssh/id_ed25519.pub");
        logger.info("公钥文件：{}", file.toAbsolutePath());

        String pubKeyText = Files.readString(file).trim();
        logger.info("公钥内容：{}", pubKeyText);

        // Base64 无 padding
        String fingerprintHash = calculateFingerprint(pubKeyText);
        logger.info("公钥指纹：{}", fingerprintHash);
    }

    /**
     * 计算 SSH（RSA/ED25519）公钥的 SHA256 指纹.
     *
     * <p>OpenSSH 的指纹是基于特定 SSH 二进制格式（RFC 4253）的 Base64 编码，而非 Java 默认的 X.509/ASN.1 格式。</p>
     * <p>
     * OpenSSH 计算指纹的流程是：
     * <ol>
     *   <li>从公钥行中提取 Base64 编码的 key blob (第二个字段)</li>
     *   <li>对 key blob 做 SHA-256 哈希</li>
     *   <li>将哈希结果转换为 OpenSSH 风格的 Base64 编码，去除末尾 '=' 填充，并加上 SHA256: 前缀。</li>
     * </ol>
     * </p>
     *
     * @param sshPublicKeyLine SSH（RSA/ED25519）公钥行，如 {@code ssh-ed25519 AAAAC3... user@host}
     * @return 指纹
     * @throws NoSuchAlgorithmException 算法异常
     * @since 2026-10-22 14:42 新建
     */
    public static String calculateFingerprint(String sshPublicKeyLine) throws NoSuchAlgorithmException {
        String[] parts = sshPublicKeyLine.trim().split("\\s+");
        if (parts.length < 2 || (!parts[0].equals("ssh-rsa") && !parts[0].equals("ssh-ed25519"))) {
            throw new IllegalArgumentException("不是有效的SSH（RSA/ED25519）公钥格式");
        }

        // 1. 从公钥行中提取 Base64 编码的 key blob（第二个字段）
        byte[] keyBlob = Base64.getDecoder().decode(parts[1]);

        // 2. 对 keyBlob 做 SHA-256 哈希
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(keyBlob);

        // 3. 转换为 OpenSSH 风格的 Base64 编码（去除末尾 '=' 填充）
        String fingerprint = Base64.getEncoder().withoutPadding().encodeToString(hashBytes);

        return "SHA256:" + fingerprint;
    }

    private String userHome() {
        return System.getProperty("user.home");
    }

}
