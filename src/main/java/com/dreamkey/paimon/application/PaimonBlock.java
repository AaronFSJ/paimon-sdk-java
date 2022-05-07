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
 * 区块管理中提供对区块的查询以及对区块上交易的查询操作
 *
 * @author WangHaoquan
 * @date 2022/3/17
 */
public class PaimonBlock extends Paimon {


    private final String blocksPath = StaticConstant.UrlGroup.DOMAINS + config.getDomain() + "/blocks/";

    public PaimonBlock(PaimonConfig config, Session session) {
        super(config, session);
    }

    /**
     * 指定范围查询区块
     *
     * @param from 当前页
     * @param to   每页大小
     * @return
     * @throws IOException
     */
    public ResponseEntity queryBlock(Integer from, Integer to) throws IOException {
        // 封装请求体
        JSONObject body = new JSONObject(2);
        body.put("from", from);
        body.put("to", to);
        String requestBody = JSONObject.toJSONString(body);
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.POST, blocksPath, requestBody, config, session.getSession());
        // 拼接得到 url，发送 POST 请求
        String url = config.getAddress() + blocksPath;
        return OkHttpUtil.builder().doPost(url, header, requestBody);
    }

    /**
     * 根据区块ID获取单个区块的具体信息
     *
     * @param blockId 区块ID
     * @return
     * @throws IOException
     */
    public ResponseEntity getBlock(String blockId) throws IOException {
        String uri = blocksPath + blockId;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.GET, uri, "", config, session.getSession());

        String url =  config.getAddress() + uri;
        return OkHttpUtil.builder().doGet(url, header, null);
    }

    /**
     * 查询指定区块中包含的交易信息（返回 data 为交易编号集合）
     *
     * @param blockId 区块ID
     * @param query   包含 offset、limit，分页偏移量和记录数
     * @return
     */
    public ResponseEntity queryTransaction(String blockId, BaseQuery query) throws IOException {
        String uri = blocksPath + blockId + "/transactions/";
        // 封装请求体
        String requestBody = JSONObject.toJSONString(query);
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.POST, uri, requestBody, config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPost(url, header, requestBody);
    }

    /**
     * 查询某个区块中某个交易的详情
     *
     * @param blockId       区块ID
     * @param transactionId 交易ID
     * @return
     * @throws IOException
     */
    public ResponseEntity getTransaction(String blockId, String transactionId) throws IOException {
        String uri = blocksPath + blockId + "/transactions/" + transactionId;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.GET, uri, "", config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doGet(url, header, null);
    }

}
