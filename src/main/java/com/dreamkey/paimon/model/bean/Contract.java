package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 合约信息
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
@Data
public class Contract {

    private String name;

    private Integer version;

    private String modifiedTime;

    private Boolean enabled;
}
