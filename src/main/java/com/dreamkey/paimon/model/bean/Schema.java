package com.dreamkey.paimon.model.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 封装资产类型（schema）信息
 *
 * @author WangHaoquan
 * @date 2022/3/17
 */
@Data
@Builder
@AllArgsConstructor
public class Schema {

    private String name;

    private List<SchemaProperty> properties;

}
