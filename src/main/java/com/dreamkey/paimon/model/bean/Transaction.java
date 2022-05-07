package com.dreamkey.paimon.model.bean;

import lombok.Data;

import java.util.List;

/**
 * 封装交易具体信息
 *
 * @author WangHaoquan
 * @date 2022/3/20
 */
@Data
public class Transaction {
    /**
     * 交易编号
     */
    private String transaction;

    /**
     * 所属区块ID
     */
    private String block;

    /**
     * 世界状态版本
     */
    private Integer world;

    /**
     * 是否为有效交易
     */
    private Boolean validated;

    /**
     * 交易提交节点
     */
    private String invoker;

    /**
     * 操作方式
     */
    private String method;

    /**
     * 交易产生时间
     */
    private String timestamp;

    /**
     * 交易包含的操作记录
     */
    private RecordSet recordSets;

    /**
     * 交易哈希
     */
    private String hash;

    /**
     * 交易哈希算法
     */
    private String hashMethod;

    /**
     * 签名
     */
    private String signature;

    /**
     * 签名算法
     */
    private String signatureMethod;

    /**
     * 背书信息
     */
    private List<Endorsement> endorsements;

}
