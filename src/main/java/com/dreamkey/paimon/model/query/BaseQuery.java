package com.dreamkey.paimon.model.query;

import lombok.Data;

/**
 * @author WangHaoquan
 * @date 2022/3/22
 */
@Data
public class BaseQuery {
    /**
     * 分页起始数
     */
    private Integer offset;

    /**
     * 分页每页页数
     */
    private Integer limit;

}
