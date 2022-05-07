package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 封装域状态信息
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
@Data
public class DomainStatus {
    /**
     * 世界状态版本
     */
    private Integer worldVersion;

    /**
     * 区块高度
     */
    private Integer blockHeight;

    /**
     * 当前（最新）区块的上一个区块ID
     */
    private String previousBlock;

    /**
     * 创世区块
     */
    private String genesisBlock;

    /**
     * 分配交易ID
     */
    private String allocatedTransactionId;
}
