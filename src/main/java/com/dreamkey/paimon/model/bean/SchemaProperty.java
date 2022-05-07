package com.dreamkey.paimon.model.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 资产类型属性字段封装类
 *
 * @author WangHaoquan
 * @date 2022/3/10
 */
@Data
@Builder
@AllArgsConstructor
public class SchemaProperty {
    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 是否索引
     */
    private Boolean indexed;
}
