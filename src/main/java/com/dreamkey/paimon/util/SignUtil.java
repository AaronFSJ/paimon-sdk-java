package com.dreamkey.paimon.util;

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

/**
 * 生成访问 paimon 的签名凭证工具
 *
 * @author WangHaoquan
 * @date 2022/3/9
 */
public class SignUtil {

    /**
     * ed25519 加密
     * 目前流行的 ed25519 加密工具包所提供的私钥分为两种
     * 1.扩展型私钥（expanded private key），长度为 64 位，此类私钥将公钥信息也包含在私钥中，以便仅用私钥便可以进行加密和验签
     * 2.独立型私钥，长度为 32 位，此类私钥需要和公钥及签名体一起方可进行验签。paimon 服务端提供的是经 hex 编码的 64 位扩展型私钥，在一些工具包中，倘若不支持 64 位扩展型私钥，则需要手动将其转为 32 位独立型私钥。
     *
     * @param key              扩展型私钥
     * @param signatureContent 签名体
     * @return
     */
    public static String ed25519(String key, String signatureContent) {
        // 注册 BouncyCastle
        Security.addProvider(new BouncyCastleProvider());
        // 将服务端提供的私钥进行 hex 解码
        byte[] privateKeyEncoded = hexStringToBytes(key);
        // 将服务端提供的 64 位扩展型私钥转为 32 位独立型私钥
        Ed25519PrivateKeyParameters privateKeyRebuild = new Ed25519PrivateKeyParameters(privateKeyEncoded, 0);
        // 将 JSON 格式的签名内容转为 bate[] 字节数组
        byte[] message = signatureContent.getBytes(StandardCharsets.UTF_8);
        // 使用私钥初始化签名对象
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, privateKeyRebuild);
        // 对内容进行签名
        signer.update(message, 0, message.length);
        byte[] sign = signer.generateSignature();
        // 签名结果进行 base64 转码
        return Base64.getEncoder().encodeToString(sign);
    }

    /**
     * 二进制转字节数组
     *
     * @param hexString hex 编码
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        String hex = hexString.toUpperCase();
        int length = hex.length() / 2;
        char[] hexChars = hex.toCharArray();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            bytes[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return bytes;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
