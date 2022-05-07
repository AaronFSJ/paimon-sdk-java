package com.dreamkey.paimon.model.query;

import lombok.Data;

import java.util.List;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 5:11 PM 2022/3/20
 * @modified by:
 */
@Data
public class DocumentQuery extends BaseQuery {

    /**
     * 查询条件
     */
    private List<QueryFilter> filters;

    /**
     * 排序条件
     */
    private String order;

    /**
     * 升降序,是否降序排列
     */
    private boolean descend;

    /**
     * 起始条件
     */
    private String since;

}
