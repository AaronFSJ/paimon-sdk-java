package com.dreamkey.paimon.util;

import cn.hutool.core.bean.BeanUtil;
import com.dreamkey.paimon.exception.PaimonException;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author WangHaoquan
 * @date 2022/3/11
 */
public class CommonUtil {

    /**
     * 获取当前时间（UTC）
     *
     * @return
     */
    public static String getUtcTime() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        long timeInMillis = calendar.getTimeInMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        return df.format(timeInMillis);
    }

    /**
     * 对象属性值复制（忽略值为 null 的属性）
     *
     * @param source
     * @param target
     */
    public static void copyNotNullProperties(Object source, Object target) {
        String[] ignoreProperties;
        try {
            ignoreProperties = getNullProperties(source);
        } catch (IllegalAccessException e) {
            throw new PaimonException("copy properties error");
        }
        BeanUtil.copyProperties(source, target, ignoreProperties);
    }

    /**
     * 获取对象值为 null 的属性数组
     *
     * @param source
     * @return
     * @throws IllegalAccessException
     */
    private static String[] getNullProperties(Object source) throws IllegalAccessException {
        Class<?> clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<String> nullFields = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(source);
            if (o == null) {
                nullFields.add(field.getName());
            }
        }

        return nullFields.toArray(new String[0]);
    }

}
