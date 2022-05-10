package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.application.PaimonContract;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.model.bean.*;
import com.dreamkey.paimon.model.query.BaseQuery;
import com.dreamkey.paimon.util.AssertUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;

/**
 * 操作智能合约 API
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
public class ContractClient {

    private final PaimonConfig config;

    public ContractClient(PaimonConfig config) {
        this.config = config;
    }


    /**
     * 智能合约分页查询
     *
     * @param page 当前页码
     * @param size 每页记录数
     * @return 返回 data 是合约基本信息集合
     * @throws IOException
     */
    public PageInfo<Contract> queryContract(Integer page, Integer size) throws IOException {
        AssertUtil.notNull(page, AssertUtil.notNullTemplate("page"));
        AssertUtil.notNull(size, AssertUtil.notNullTemplate("size"));
        AssertUtil.isTrue(page > 0 && size > 0, "'page' and 'size' must be greater than 0");

        BaseQuery query = new BaseQuery();
        query.setOffset((page - 1) * size);
        query.setLimit(size);

        Session session = PaimonUtil.getSession(config);
        PaimonContract paimonContract = new PaimonContract(config, session);
        ResponseEntity response = paimonContract.queryContract(query);

        JSONObject data = JSONObject.parseObject(response.getData());

        return new PageInfo<>(data.getInteger("total"),page,size,
                data.getJSONArray("contracts").toJavaList(Contract.class));
    }

    /**
     * 部署智能合约
     *
     * @param name    合约名称
     * @param content 合约内容（json字符串，具体格式参考智能合约开发手册）
     * @throws IOException
     */
    public void deployContract(String name, String content) throws IOException {
        AssertUtil.hasText(name, AssertUtil.hasTextTemplate("name"));
        AssertUtil.hasText(content, AssertUtil.hasTextTemplate("content"));

        Session session = PaimonUtil.getSession(config);
        PaimonContract paimonContract = new PaimonContract(config, session);
        paimonContract.deployContract(name, content);
    }

    /**
     * 撤销智能合约
     *
     * @param name 合约名称
     * @throws IOException
     */
    public void cancelContract(String name) throws IOException {
        AssertUtil.hasText(name, AssertUtil.hasTextTemplate("name"));

        Session session = PaimonUtil.getSession(config);
        PaimonContract paimonContract = new PaimonContract(config, session);
        paimonContract.cancelContract(name);
    }

    /**
     * 调用智能合约
     *
     * @param name       合约名称
     * @param parameters 合约参数
     * @throws IOException
     */
    public void callContract(String name, String... parameters) throws IOException {
        AssertUtil.hasText(name, AssertUtil.hasTextTemplate("name"));
        AssertUtil.notEmpty(parameters, AssertUtil.notEmptyTemplate("parameters"));

        Session session = PaimonUtil.getSession(config);
        PaimonContract paimonContract = new PaimonContract(config, session);
        paimonContract.callContract(name, parameters);
    }

    /**
     * 查询合约内容
     *
     * @param name 合约名称
     * @return 合约名称和具体内容（json字符串）
     * @throws IOException
     */
    public ContractDetail getContractDetail(String name) throws IOException {
        AssertUtil.hasText(name, AssertUtil.hasTextTemplate("name"));

        Session session = PaimonUtil.getSession(config);
        PaimonContract paimonContract = new PaimonContract(config, session);
        ResponseEntity response = paimonContract.getContract(name);

        JSONObject data = JSONObject.parseObject(response.getData());
        return JSONObject.toJavaObject(data, ContractDetail.class);
    }

    /**
     * 开启合约调试模式
     *
     * @param name   合约名称
     * @param enable 是否开启调试（开启调试系统日志将输出合约执行流程）
     * @throws IOException
     */
    public void traceContract(String name, Boolean enable) throws IOException {
        AssertUtil.hasText(name, AssertUtil.hasTextTemplate("name"));
        AssertUtil.notNull(enable, AssertUtil.notNullTemplate("enable"));

        Session session = PaimonUtil.getSession(config);
        PaimonContract paimonContract = new PaimonContract(config, session);
        paimonContract.traceContract(name, enable);
    }

}
