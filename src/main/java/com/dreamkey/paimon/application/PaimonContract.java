package com.dreamkey.paimon.application;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.common.enumerate.RequestMethod;
import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.model.query.BaseQuery;
import com.dreamkey.paimon.util.OkHttpUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * 操作合约（contract）
 *
 * @author WangHaoquan
 * @date 2022/3/10
 */
public class PaimonContract extends Paimon {

    private final String contractPath = StaticConstant.UrlGroup.DOMAINS + config.getDomain() + "/contracts/";

    public PaimonContract(PaimonConfig config, Session session) {
        super(config, session);
    }

    /**
     * 分页查询合约列表
     *
     * @param query
     * @return
     * @throws IOException
     */
    public ResponseEntity queryContract(BaseQuery query) throws IOException {
        // 封装请求体，创建请求头
        String requestBody = JSONObject.toJSONString(query);
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.POST, contractPath, requestBody, config, session.getSession());
        // 发送请求
        String url = config.getAddress() + contractPath;
        return OkHttpUtil.builder().doPost(url, header, requestBody);
    }

    /**
     * 部署合约，合约不可更新，可撤销重新部署
     *
     * @param name
     * @param content
     * @return
     */
    public ResponseEntity deployContract(String name, String content) throws IOException {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("content", content);
        String requestBody = JSONObject.toJSONString(bodyJson);

        String uri = contractPath + name;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.PUT, uri, requestBody, config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPut(url, header, requestBody);
    }

    /**
     * 撤销合约
     *
     * @param name
     */
    public ResponseEntity cancelContract(String name) throws IOException {
        String uri = contractPath + name;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.DELETE, uri, "", config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doDelete(url, header, null);
    }

    /**
     * 调用合约
     *
     * @param name       合约名称
     * @param parameters 合约执行参数
     */
    public ResponseEntity callContract(String name, String... parameters) throws IOException {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("parameters", parameters);
        String requestBody = JSONObject.toJSONString(bodyJson);

        String uri = contractPath + name + "/sessions/";
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.POST, uri, requestBody, config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPost(url, header, requestBody);
    }

    /**
     * 查看合约详情
     *
     * @param name 合约名称
     * @return
     * @throws IOException
     */
    public ResponseEntity getContract(String name) throws IOException {
        String uri = contractPath + name;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.GET, uri, "", config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doGet(url, header, null);
    }

    /**
     * 合约调试开关
     *
     * @param name   合约名称
     * @param enable 是否开启调试，true开启、false关闭（开启调试日志会打印合约执行步骤内容）
     */
    public ResponseEntity traceContract(String name, Boolean enable) throws IOException {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("enable", enable);
        String requestBody = JSONObject.toJSONString(bodyJson);

        String uri = contractPath + name + "/trace/";
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.PUT, uri, requestBody, config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPut(url, header, requestBody);
    }

}
