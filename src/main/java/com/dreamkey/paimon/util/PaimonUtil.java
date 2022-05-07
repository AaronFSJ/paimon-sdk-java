package com.dreamkey.paimon.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.common.enumerate.RequestMethod;
import com.dreamkey.paimon.exception.PaimonException;
import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Session;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Paimon 工具类
 *
 * @author WangHaoquan
 * @date 2022/3/11
 */
public class PaimonUtil {

    private static Logger logger = Logger.getLogger(PaimonUtil.class.toString());

    // 10 分钟，Paimon 默认 15 分钟
    private static final int expireTime = 10 * 60;


    public static Session getSession(PaimonConfig config) throws IOException {
        String cacheKey = config.getAccessId() +"-"+config.getAddress();
        LocalCache localCache = LocalCache.getInstance();
        Object value = localCache.getValue(cacheKey);
        Session session = null;
        if(Objects.isNull(value)) {
            logger.log(Level.INFO,"调用 Paimon 获取 Session");
            session = createSession(config);
            localCache.putValue(cacheKey,session,expireTime);
        }else {
            logger.log(Level.INFO,"从缓存获取 Session");
            session = (Session)value;
        }
        return session;
    }

    public static Session createSession(PaimonConfig config) throws IOException {
        String timestamp = CommonUtil.getUtcTime();
        // 封装签名内容
        JSONObject content = new JSONObject(true);
        content.put("access", config.getAccessId());
        content.put("timestamp", timestamp);
        content.put("nonce", StaticConstant.NONCE);
        content.put("signature_algorithm", StaticConstant.SIGNATURE_ALGORITHM);
        String signatureContent = content.toJSONString();

        // 使用 ed25519 加密
        String signature = SignUtil.ed25519(config.getSecretKey(), signatureContent);

        // 组装请求头与请求体
        Map<String, String> header = new HashMap<>(3);
        header.put("Paimon-Timestamp", timestamp);
        header.put("Paimon-SignatureAlgorithm", StaticConstant.SIGNATURE_ALGORITHM);
        header.put("Paimon-Signature", signature);

        Map<String, Object> paramMap = new HashMap<>(2);
        paramMap.put("id", config.getAccessId());
        paramMap.put("nonce", StaticConstant.NONCE);
        String body = JSON.toJSONString(paramMap);

        String url = config.getAddress() + StaticConstant.UrlGroup.SESSIONS;

        OkHttpUtil httpUtil = OkHttpUtil.builder();
        ResponseEntity response = httpUtil.doPost(url, header, body);

        return httpUtil.convertDataStringToEntity(response, Session.class);
    }

    /**
     * 刷新session，延长有效时间
     *
     * @param session 会话
     * @throws IOException
     */
    public static void refreshSession(String session, PaimonConfig config) throws IOException {
        AssertUtil.hasText(session, AssertUtil.hasTextTemplate("session"));

        String uri = StaticConstant.UrlGroup.SESSIONS;
        Map<String, String> header = createHeader(RequestMethod.PUT, uri, "{}", config, session);
        String url = config.getAddress() + StaticConstant.UrlGroup.SESSIONS;

        OkHttpUtil httpUtil = OkHttpUtil.builder();
        ResponseEntity response = httpUtil.doPut(url, header, "{}");
        if(!StaticConstant.ERROR_CODE_OK.equals(response.getErrorCode())) {
            throw new PaimonException("refresh session Error");
        }
    }

    /**
     * 创建通用请求头
     *
     * @param method      请求方式
     * @param uri         请求 uri
     * @param requestBody 请求体
     * @return
     * @throws IOException
     */
    public static Map<String, String> createHeader(RequestMethod method, String uri, String requestBody, PaimonConfig config, String session) throws IOException {

        String body = "";
        if (!RequestMethod.GET.equals(method) && !RequestMethod.HEAD.equals(method) && requestBody != null) {
            // 使用 cryptoJS 进行 sha256 加密
            body = Base64.getEncoder().encodeToString(ShaUtil.sha256(requestBody));
        }
        String timestamp = CommonUtil.getUtcTime();
        // 组装签名体，paimon 验签时校验严格，需要用 linkMap 保持排序
        JSONObject signatureContent = new JSONObject(true);
        signatureContent.put("id", session);
        signatureContent.put("method", method.name());
        signatureContent.put("url", uri);
        signatureContent.put("body", body);
        signatureContent.put("access", config.getAccessId());
        signatureContent.put("timestamp", timestamp);
        signatureContent.put("nonce", StaticConstant.NONCE);
        signatureContent.put("signature_algorithm", StaticConstant.SIGNATURE_ALGORITHM);

        // 执行签名
        String signature = SignUtil.ed25519(config.getSecretKey(), signatureContent.toJSONString());
        // 组装请求头
        Map<String, String> header = new HashMap<>(4);
        header.put("Paimon-Session", session);
        header.put("Paimon-Timestamp", timestamp);
        header.put("Paimon-SignatureAlgorithm", StaticConstant.SIGNATURE_ALGORITHM);
        header.put("Paimon-Signature", signature);

        return header;
    }

}
