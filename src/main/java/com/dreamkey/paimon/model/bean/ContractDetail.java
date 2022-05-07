package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 合约内容详情
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
@Data
public class ContractDetail {
    /**
     * 合约名称（唯一）
     */
    private String name;

    /**
     * 合约内容
     */
    private String content;
}
