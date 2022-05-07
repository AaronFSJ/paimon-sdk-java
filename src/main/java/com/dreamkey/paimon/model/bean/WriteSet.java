package com.dreamkey.paimon.model.bean;

import lombok.Data;

/**
 * 交易包含的写结果
 *
 * @author WangHaoquan
 * @date 2022/3/20
 */
@Data
public class WriteSet {

    private Integer version;

    /**
     * 操作的文档类型
     */
    private String schema;

    /**
     * 如果操作的是文档，该值操作的文档ID
     */
    private String id;

    /**
     * 变更内容（文档类型信息或文档数据）
     */
    private Object content;
}
