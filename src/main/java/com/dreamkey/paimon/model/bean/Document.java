package com.dreamkey.paimon.model.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 4:58 PM 2022/3/20
 * @modified by:
 */
@Data
@Builder
@AllArgsConstructor
public class Document {

    private String id;

    private String content;

}
