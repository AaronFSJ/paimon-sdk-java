package com.dreamkey.paimon.common.enumerate;

/**
 * 资产类型字段对应类型
 *
 * @author WangHaoquan
 * @date 2022/3/22
 */
public enum PropertyType {

    /**
     * 字符串类型
     */
    STRING("string"),

    /**
     * 整型
     */
    INT("int"),

    /**
     * 布尔类型
     */
    BOOL("bool"),

    /**
     * 浮点类型
     */
    FLOAT("float"),

    /**
     * 货币类型（要求值不小于 0）
     */
    CURRENCY("currency");


    private final String type;

    PropertyType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
