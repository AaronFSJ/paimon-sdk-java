package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.application.PaimonBlock;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.model.bean.*;
import com.dreamkey.paimon.model.query.BaseQuery;
import com.dreamkey.paimon.util.AssertUtil;
import com.dreamkey.paimon.util.CommonUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;

/**
 * 查询区块和交易信息
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
public class BlockClient {

    private final PaimonConfig config;

    public BlockClient(PaimonConfig config) {
        this.config = config;
    }

    /**
     * 分页查询区块
     *
     * @param page 当前页码
     * @param size 每页记录数量
     * @return 返回 data 是区块 hash 集合
     * @throws IOException
     */
    public PageInfo<String> queryBlock(Integer page, Integer size) throws IOException {
        AssertUtil.notNull(page, AssertUtil.notNullTemplate("page"));
        AssertUtil.notNull(size, AssertUtil.notNullTemplate("size"));
        AssertUtil.isTrue(page > 0 && size > 0, "'page' and 'size' must be greater than 0");

        Session session = PaimonUtil.getSession(config);
        PaimonBlock paimonBlock = new PaimonBlock(config, session);

        Integer from = (page - 1) * size + 1;
        Integer to = from + size - 1;
        ResponseEntity response = paimonBlock.queryBlock(from, to);

        JSONObject data = JSONObject.parseObject(response.getData());

        return new PageInfo<>(data.getInteger("height"),page,size,
                data.getJSONArray("blocks").toJavaList(String.class));
    }

    /**
     * 获取区块信息
     *
     * @param blockId 区块 ID（哈希）
     * @return 区块信息
     * @throws IOException
     */
    public Block getBlock(String blockId) throws IOException {
        AssertUtil.hasText(blockId, AssertUtil.hasTextTemplate("blockId"));

        Session session = PaimonUtil.getSession(config);
        PaimonBlock paimonBlock = new PaimonBlock(config, session);
        ResponseEntity response = paimonBlock.getBlock(blockId);

        JSONObject data = JSONObject.parseObject(response.getData());
        Block blockInfo = JSONObject.toJavaObject(data, Block.class);

        String content = data.getString("content");
        JSONObject dataContent = JSONObject.parseObject(content);
        dataContent.remove("transactions");
        Block block = JSONObject.toJavaObject(dataContent, Block.class);

        CommonUtil.copyNotNullProperties(blockInfo, block);

        return block;
    }

    /**
     * 区块中的交易分页查询
     *
     * @param blockId 区块 ID（哈希）
     * @param page    当前页码
     * @param size    每页记录数
     * @return 返回 data 是交易 ID 集合
     * @throws IOException
     */
    public PageInfo<String> queryTransaction(String blockId, Integer page, Integer size) throws IOException {
        AssertUtil.hasText(blockId, AssertUtil.hasTextTemplate("blockId"));
        AssertUtil.notNull(page, AssertUtil.notNullTemplate("page"));
        AssertUtil.notNull(size, AssertUtil.notNullTemplate("size"));

        Session session = PaimonUtil.getSession(config);
        PaimonBlock paimonBlock = new PaimonBlock(config, session);

        BaseQuery query = new BaseQuery();
        query.setOffset((page - 1) * size);
        query.setLimit(size);
        ResponseEntity response = paimonBlock.queryTransaction(blockId, query);

        JSONObject data = JSONObject.parseObject(response.getData());
        return new PageInfo<>(data.getInteger("total"),page,size,
                data.getJSONArray("transactions").toJavaList(String.class));
    }


    /**
     * 获取交易详情
     *
     * @param blockId       区块 ID
     * @param transactionId 交易 ID
     * @return 交易详情
     * @throws IOException
     */
    public Transaction getTransaction(String blockId, String transactionId) throws IOException {
        AssertUtil.hasText(blockId, AssertUtil.hasTextTemplate("blockId"));

        Session session = PaimonUtil.getSession(config);
        PaimonBlock paimonBlock = new PaimonBlock(config, session);
        ResponseEntity response = paimonBlock.getTransaction(blockId, transactionId);

        JSONObject data = JSONObject.parseObject(response.getData());
        Transaction transactionInfo = JSONObject.toJavaObject(data, Transaction.class);

        JSONObject content = JSONObject.parseObject(data.getString("content"));
        Transaction transaction = JSONObject.toJavaObject(content, Transaction.class);

        CommonUtil.copyNotNullProperties(transactionInfo, transaction);

        return transaction;
    }
}
