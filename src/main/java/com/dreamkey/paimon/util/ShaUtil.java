package com.dreamkey.paimon.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA 哈希加密算法工具
 *
 * @author WangHaoquan
 * @date 2022/3/9
 */
public class ShaUtil {

    /**
     * sha256 加密
     *
     * @param str 加密内容
     * @return
     */
    public static byte[] sha256(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ignore) {
        }
        messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        return messageDigest.digest();
    }
}
