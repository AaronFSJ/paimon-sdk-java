package com.dreamkey.paimon.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Aaron
 * @description: 使用该注解标记 Paimon Schema 相关信息
 * @date: Create in 11:53 AM 2022/5/6
 * @modified by:
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface PaimonSchema {

    String name();
}
