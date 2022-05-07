package com.dreamkey.paimon.model.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装获取会话时的返回数据
 *
 * @author WangHaoquan
 * @date 2022/3/16
 */
@Data
public class Session implements Serializable {

    private String address;

    private String session;

    private Integer timeout;
}
