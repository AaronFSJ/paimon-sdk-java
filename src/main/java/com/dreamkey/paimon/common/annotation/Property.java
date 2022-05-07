package com.dreamkey.paimon.common.annotation;

import com.dreamkey.paimon.common.enumerate.PropertyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用该注解标记资产类型在 paimon 中的对应类型
 *
 * @author WangHaoquan
 * @date 2022/3/22
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface Property {

    PropertyType type();

    boolean indexed() default false;
}
