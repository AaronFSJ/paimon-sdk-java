package com.dreamkey.paimon;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.util.PaimonUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author WangHaoquan
 * @date 2022/3/29
 */
public class PaimonBaseTest {

    public PaimonConfig config = new PaimonConfig("system","192.168.1.84:9100", "c91d3bv29928uoh6a5h0", "6cae6cb76ed7a9c8b1e724d29c5c4744f1d135719096de676c6a7d2ff128a4abcdd1a0a8c99c1a8ebd7fd87fcdc7a42d57163cf17154e7c8319e397821bc5dbf");

    @Test
    public void sessionTest() throws IOException, InterruptedException {
//        Session session = PaimonUtil.createSession(config);
//        System.out.println(JSONObject.toJSONString(session));
//        PaimonUtil.refreshSession(session.getSession(), config);


        Session session = PaimonUtil.getSession(config);
        System.out.println(JSONObject.toJSONString(session));
        Thread.sleep(1000);
        session = PaimonUtil.getSession(config);
        System.out.println(JSONObject.toJSONString(session));

    }
}
