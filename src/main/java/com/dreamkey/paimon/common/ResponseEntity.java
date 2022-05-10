package com.dreamkey.paimon.common;

import lombok.Data;

/**
 * 统一的数据返回实体
 *
 * @author WangHaoquan
 * @date 2022/3/9
 */
@Data
public class ResponseEntity {

    private Integer errorCode;

    private String message;

    private String data;



    public ResponseEntity(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ResponseEntity(Integer errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ResponseEntity(Integer errorCode, String message, String data) {
        this.errorCode = errorCode;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity ok(){
        return new ResponseEntity(StaticConstant.ERROR_CODE_OK);
    }

    public static ResponseEntity error(){
        return new ResponseEntity(StaticConstant.ERROR_CODE_ERROR);
    }

}
