/*
 * Copyright © 2011-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0.
 * See `LICENSE` in the project root for license information.
 */

package me.ijleex.dev.test;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 解析X.509证书（签名证书/加密证书）.
 *
 * @author liym
 * @since 2026-05-15 14:09 新建
 */
public class X509CertificateTest {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * X.509证书工厂
     */
    private static final CertificateFactory CERTIFICATE_FACTORY;

    public X509CertificateTest() {
    }

    @Test
    public void parse_ecc() throws CertificateException {
        // 已去除-----BEGIN/END CERTIFICATE-----的纯Base64编码X.509证书字符串
        String signCert = "MIICKzCCAdGgAwIBAgIUYvgB8BM4mLqg3SypMODwxSdCxxwwCgYIKoZIzj0EAwIwgYMxHzAdBgkqhkiG9w0BCQEWEGlqbGl5bUBpamxlZXgubWUxFDASBgNVBAMMCyouaWpsZWV4Lm1lMRIwEAYDVQQLDAlpamxlZXgubWUxDzANBgNVBAoMBmlqbGl5bTELMAkGA1UEBwwCSEQxCzAJBgNVBAgMAkJKMQswCQYDVQQGEwJDTjAeFw0yNTA4MTYxMjAwMThaFw0yNjA4MTYxMjAwMThaMIGDMR8wHQYJKoZIhvcNAQkBFhBpamxpeW1AaWpsZWV4Lm1lMRQwEgYDVQQDDAsqLmlqbGVleC5tZTESMBAGA1UECwwJaWpsZWV4Lm1lMQ8wDQYDVQQKDAZpamxpeW0xCzAJBgNVBAcMAkhEMQswCQYDVQQIDAJCSjELMAkGA1UEBhMCQ04wWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAQI6INKfIn+ET0HYrt6Inbcjcfo0dYZUs07tpO9zTrO3soRwcxNc0/yA7WgqtmV+RIfgP7IVykk5ngr/OB6Q85boyEwHzAdBgNVHQ4EFgQU6La3rZ7r80pt72BPZ/TlUqwPWSUwCgYIKoZIzj0EAwIDSAAwRQIgMKTzzaV/JnBj/FG8Cv9KesL/0FiNkd+HAKHSewrqlEoCIQDrKhugJS3GP69IKu+s3wJ9Mj71XXT0TTUBji8CVUa6DA==";
        byte[] certBytes = Base64.getDecoder().decode(signCert);
        X509Certificate cert = (X509Certificate) CERTIFICATE_FACTORY.generateCertificate(new ByteArrayInputStream(certBytes));
        this.printCertInfo(cert);
    }

    /**
     * 打印证书的所有核心信息
     *
     * @param cert 已解析的X509证书对象
     * @see sun.security.jca.JCAUtil#tryCommitCertEvent(java.security.cert.Certificate)
     */
    public void printCertInfo(X509Certificate cert) {
        try {
            logger.info("========== X.509 证书详细信息 ==========");
            long certId = Integer.toUnsignedLong(cert.hashCode());
            logger.info("证书ID：certId={}", certId);
            logger.info("版本：version=V{}", cert.getVersion());
            BigInteger serialNumber = cert.getSerialNumber();
            logger.info("序列号：serialNo={}", serialNumber.toString(16));
            logger.info("签名算法：sigAlgName={}", cert.getSigAlgName());
            logger.info("颁发者：issuer={}", cert.getIssuerX500Principal().toString());
            logger.info("使用者：subject={}", cert.getSubjectX500Principal().toString());
            Date validFrom = cert.getNotBefore(), validUntil = cert.getNotAfter();
            logger.info("有效期始：validFrom={}", DATE_FORMAT.format(validFrom));
            logger.info("有效期止：validUntil={}", DATE_FORMAT.format(validUntil));
            PublicKey publicKey = cert.getPublicKey();
            byte[] publicKeyBytes = publicKey.getEncoded();
            logger.info("公钥：publicKey={}", toString(publicKeyBytes));
            logger.info("公钥算法：publicKeyAlg={}", publicKey.getAlgorithm());
            logger.info("指纹：fingerprint={}", this.getCertFingerprint(cert, "SHA-1"));
            logger.info("指纹：fingerprint={}", this.getCertFingerprint(cert, "SHA-256"));
        } catch (Exception e) {
            logger.error("printCertInfo error", e);
        }
    }

    /**
     * 计算证书指纹（MD5/SHA1/SHA256）
     */
    private String getCertFingerprint(X509Certificate cert, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] digest = md.digest(cert.getEncoded());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02X:", b));
        }
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String toString(byte[] b) {
        if (b == null) {
            return "(null)";
        }
        return HexFormat.ofDelimiter(":").formatHex(b);
    }

    public static String toString(BigInteger b) {
        return toString(b.toByteArray());
    }

    static {
        try {
            CERTIFICATE_FACTORY = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        }
    }
}
