package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.common.enumerate.RequestMethod;
import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.util.OkHttpUtil;
import com.dreamkey.paimon.util.PaimonUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 9:30 PM 2022/3/17
 * @modified by:
 */
public class OkHttpUtilTest {

    @ValueSource(strings = {"/application/schema/querySchemasTest.json"})
    @ParameterizedTest
    public void postTest(String str) {

        PaimonConfig config = new PaimonConfig("system", "192.168.1.84:9100", "c8o4ikn2992bl1sdkn5g",
                "f5b4565c0e74384b05765fa298346048833ac99711003fe1ae9f24b1614182707c9d1d67c09f958fe98d425dc48db0a98d68609a2737a3c1e3a8985cbc683b99");

        JSONObject arg = TestUtils.getTestArg(str);
        Integer page = arg.getInteger("page");
        Integer size = arg.getInteger("size");

        Session session;
        try {
            session = PaimonUtil.createSession(config);
            String uri = StaticConstant.UrlGroup.DOMAINS + config.getDomain() + "/schemas/";

            // 组装 body
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("offset", (page - 1) * size);
            paramMap.put("limit", size);
            String body = JSON.toJSONString(paramMap);

            // 组装 headers
            Map<String, String> headers = PaimonUtil.createHeader(RequestMethod.POST, uri, body, config, session.getSession());

            String url = StaticConstant.REQUEST_PROTOCOL_PREFIX + config.getAddress() + uri;

            // 发送请求
            ResponseEntity responseEntity = OkHttpUtil.builder().doPost(url, headers, body);
            System.out.println(responseEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
