package com.dreamkey.paimon.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 5:14 PM 2022/3/20
 * @modified by:
 */
@Data
@Builder
@AllArgsConstructor
public class QueryFilter {

    /**
     * 要进行筛选的属性
     */
    private String property;

    /**
     * 筛选属性的对比操作，参考枚举类：OperatorEnum
     */
    private Integer operator;

    /**
     * 筛选属性的对比值
     */
    private String value;
}
