package com.dreamkey.paimon.model.bean;

import lombok.Data;

import java.util.List;

/**
 * 资产变更日志记录
 *
 * @author WangHaoquan
 * @date 2022/3/22
 */
@Data
public class DocumentLog {

    private Integer latestVersion;

    private List<OperationLog> logs;

}
