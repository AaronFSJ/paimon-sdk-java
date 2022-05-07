package com.dreamkey.paimon.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 封装分页数据
 *
 * @author WangHaoquan
 * @date 2022/3/17
 */
@Data
@AllArgsConstructor
public class PageInfo<T> {

    /**
     * 总记录数
     */
    private Integer total;

    /**
     * 当前所在页
     */
    private Integer page;

    /**
     * 每页大小
     */
    private Integer size;

    /**
     * 结果集
     */
    private List<T> data;

    public PageInfo() {
    }
}
