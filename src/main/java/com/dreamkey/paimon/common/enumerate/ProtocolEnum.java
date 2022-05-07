package com.dreamkey.paimon.common.enumerate;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 10:05 AM 2022/5/7
 * @modified by:
 */
public enum ProtocolEnum {


    /**
     * 字符串类型
     */
    HTTP("http"),

    /**
     * 整型
     */
    HTTPS("https");

    private final String protocol;

    ProtocolEnum(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

}
