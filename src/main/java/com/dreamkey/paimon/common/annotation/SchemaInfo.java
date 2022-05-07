package com.dreamkey.paimon.common.annotation;

import java.lang.annotation.*;

/**
 * @author: Aaron
 * @description: 使用该注解标记 Paimon Schema 相关信息
 * @date: Create in 11:53 AM 2022/5/6
 * @modified by:
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface SchemaInfo {

    String name();
}
