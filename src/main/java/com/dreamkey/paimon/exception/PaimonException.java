package com.dreamkey.paimon.exception;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 2:53 PM 2022/3/18
 * @modified by:
 */
public class PaimonException extends RuntimeException {

    private int code;

    private String message;


    public PaimonException(String message) {
        this(0, message);
    }

    public PaimonException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}
