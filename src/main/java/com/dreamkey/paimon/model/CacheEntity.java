package com.dreamkey.paimon.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 3:17 PM 2022/5/6
 * @modified by:
 */
@Data
public class CacheEntity implements Serializable {

    public CacheEntity(Object value, long gmtModify, int expire) {
        super();
        this.value = value;
        this.gmtModify = gmtModify;
        this.expire = expire;
    }

    /**
     * 值
     */
    private Object value;

    /**
     * 保存的时间戳
     */
    private long gmtModify;

    /**
     * 过期时间，秒为单位
     */
    private int expire;



}
