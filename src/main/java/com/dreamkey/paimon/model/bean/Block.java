package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 封装区块信息
 *
 * @author WangHaoquan
 * @date 2022/3/20
 */
@Data
public class Block {

    /**
     * 区块ID
     */
    private String id;

    /**
     * 区块哈希算法
     */
    private String hashMethod;

    /**
     * 区块创建时间
     */
    private String timestamp;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 默克尔根
     */
    private String root;

    /**
     * 上一个区块ID
     */
    private String previousBlock;

    /**
     * 当前区块高度
     */
    private String height;

    /**
     * 区块包含的交易数
     */
    private Integer transactions;

    /**
     * 交易签名
     */
    private String signature;

    /**
     * 签名算法
     */
    private String signatureMethod;

    /**
     * 临时随机值
     */
    private String nonce;

    /**
     * 提交节点
     */
    private String invoker;

}
