package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 交易背书信息
 *
 * @author WangHaoquan
 * @date 2022/3/20
 */
@Data
public class Endorsement {
    /**
     * 背书节点
     */
    private String endorser;

    /**
     * 节点签名
     */
    private String signature;

}
