package com.dreamkey.paimon.bean;

import com.dreamkey.paimon.common.annotation.DocId;
import com.dreamkey.paimon.common.annotation.Property;
import com.dreamkey.paimon.common.annotation.SchemaInfo;
import com.dreamkey.paimon.common.enumerate.PropertyType;
import lombok.Data;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 4:31 PM 2022/3/20
 * @modified by:
 */
@Data
@SchemaInfo(name = "user")
public class User {
    @DocId
    @Property(type = PropertyType.INT, indexed = true)
    private Integer userId;

    @Property(type = PropertyType.STRING, indexed = true)
    private String name;

    @Property(type = PropertyType.STRING)
    private String sex;

    @Property(type = PropertyType.INT)
    private Integer age;

    private String profession;
}
