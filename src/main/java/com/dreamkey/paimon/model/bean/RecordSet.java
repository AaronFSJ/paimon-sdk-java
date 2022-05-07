package com.dreamkey.paimon.model.bean;

import lombok.Data;

import java.util.List;

/**
 * 封装某个交易包含的操作记录
 *
 * @author WangHaoquan
 * @date 2022/3/20
 */
@Data
public class RecordSet {

    private List<WriteSet> writeSet;
}
