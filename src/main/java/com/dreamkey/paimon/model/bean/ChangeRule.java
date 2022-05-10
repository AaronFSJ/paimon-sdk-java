package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 涉及变更的规则（委员会、成员）
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
@Data
public class ChangeRule {

    private String domain;

    private String add;

    private String remove;
}
