package com.dreamkey.paimon.common.enumerate;

import lombok.Getter;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 5:17 PM 2022/3/20
 * @modified by:
 */
@Getter
public enum OperatorEnum {

    EQUAL(0, "="),
    NOT_EQUAL(1, "!="),
    GREATE_THAN(2, ">"),
    LESS_THAN(3, "<"),
    GREATE_EQUAL(4, ">="),
    LESS_EQUAL(5, "<=");

    private final Integer key;
    private final String value;



    OperatorEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValueByKey(Integer key) {
        OperatorEnum[] constants = OperatorEnum.values();
        for (OperatorEnum constant : constants) {
            if (constant.getKey().equals(key)) {
                return constant.getValue();
            }
        }
        return null;
    }
}
