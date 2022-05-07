package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 提议规则
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
@Data
public class ProposalRule {

    private String domain;

    private Float majority;

    private Integer all;

}
