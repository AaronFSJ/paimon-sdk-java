package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 单条资产操作记录内容
 *
 * @author WangHaoquan
 * @date 2022/3/19
 */
@Data
public class OperationLog {

    /**
     * 操作描述
     */
    private String operate;

    /**
     * 区块哈希
     */
    private String block;

    /**
     * 调用者
     */
    private String invoker;

    /**
     * 更新后的文档版本
     */
    private Integer version;

    /**
     * 	是否确认共识
     */
    private Boolean confirmed;

    /**
     * 所属交易
     */
    private String transaction;

    /**
     * 时间戳
     */
    private String timestamp;
}
