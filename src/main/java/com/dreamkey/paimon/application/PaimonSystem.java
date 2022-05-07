package com.dreamkey.paimon.application;

import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.common.enumerate.RequestMethod;
import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.util.OkHttpUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * 查看一些系统参数
 *
 * @author WangHaoquan
 * @date 2022/3/17
 */
public class PaimonSystem extends Paimon {

    public PaimonSystem(PaimonConfig config, Session session) {
        super(config, session);
    }

    /**
     * 查看当前域状态
     *
     * @return
     */
    public ResponseEntity getDomainStatus() throws IOException {
        String uri = StaticConstant.UrlGroup.DOMAINS + config.getDomain() + "/status";
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.GET, uri, "", config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doGet(url, header, null);
    }
}
