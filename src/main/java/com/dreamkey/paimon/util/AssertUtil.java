package com.dreamkey.paimon.util;

import com.dreamkey.paimon.exception.PaimonException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * 断言工具
 *
 * @author WangHaoquan
 * @date 2022/3/22
 */
public class AssertUtil {

    public static String notNullTemplate(String thing) {
        return "'" + thing + "' must not be null";
    }

    public static String hasTextTemplate(String thing) {
        return "'" + thing + "' is null or its length is not greater than 0";
    }

    public static String notEmptyTemplate(String thing) {
        return "'" + thing + "' must not be empty";
    }

    public static void notNull(@Nullable Object object, String message) {
        if (object == null) {
            throw new PaimonException(message);
        }
    }

    public static void hasText(@Nullable String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new PaimonException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new PaimonException(message);
        }
    }

    public static void notEmpty(@Nullable Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new PaimonException(message);
        }
    }

    public static void notEmpty(@Nullable Object[] array, String message) {
        if (ObjectUtils.isEmpty(array)) {
            throw new PaimonException(message);
        }
    }
}
