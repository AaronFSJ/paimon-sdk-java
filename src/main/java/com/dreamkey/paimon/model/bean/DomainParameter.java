package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 域相关参数
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
@Data
public class DomainParameter {
    /**
     * 域
     */
    private String domain;

    /**
     * 区块大小
     */
    private Integer blockSize;

    /**
     * 最大交易数量
     */
    private Integer maxTransaction;

    /**
     * 最长等待时间
     */
    private Integer maxWaitTime;

    /**
     * 交易共识
     */
    private String transactionConsensus;

    /**
     * 出块共识
     */
    private String commitConsensus;

    /**
     * 委员会大小
     */
    private Integer committeesSize;

    /**
     * 合约包含最大操作步骤数量
     */
    private Integer maxContractStep;

}
