package com.util;

import com.dreamkey.paimon.bean.User;
import com.dreamkey.paimon.common.annotation.DocId;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

/**
 * @author WangHaoquan
 * @date 2022/3/22
 */
public class AnnotationTest {

    @Test
    public void getAnnotationTest() {
        Field[] fields = User.class.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            DocId docId = f.getAnnotation(DocId.class);
            if (docId != null) {
                System.out.println(f.getName());
                System.out.println("i get it: " + docId);
            }
        }
    }

    @Test
    public void getClassNameTest() {
        String name = User.class.getSimpleName();
        System.out.println(name);
    }
}
