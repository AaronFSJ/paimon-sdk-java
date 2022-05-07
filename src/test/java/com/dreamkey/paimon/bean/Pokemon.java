package com.dreamkey.paimon.bean;

import com.dreamkey.paimon.common.annotation.DocId;
import com.dreamkey.paimon.common.annotation.Property;
import com.dreamkey.paimon.common.enumerate.PropertyType;
import lombok.Data;

/**
 * @author WangHaoquan
 * @date 2022/3/24
 */
@Data
public class Pokemon {

    @DocId
    @Property(type = PropertyType.STRING, indexed = true)
    private String id;

    @Property(type = PropertyType.STRING)
    private String species;

    @Property(type = PropertyType.INT)
    private Integer number;
}
