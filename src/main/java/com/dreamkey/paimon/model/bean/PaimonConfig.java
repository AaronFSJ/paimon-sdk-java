package com.dreamkey.paimon.model.bean;

import com.dreamkey.paimon.common.enumerate.ProtocolEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求 paimon 链配置
 *
 * @author WangHaoquan
 * @date 2022/3/10
 */
@Data
public class PaimonConfig {

    /**
     * 域
     */
    private String domain;

    /**
     * 接口地址（协议+ip+port），如 http://127.0.0.1:8080
     * 不传入协议默认 http
     */
    private String address;

    /**
     * 通行id
     */
    private String accessId;

    /**
     * 私钥，二进制
     */
    private String secretKey;

    public PaimonConfig(String domain, String address, String accessId, String secretKey) {
        this.domain = domain;
        if(address.contains(ProtocolEnum.HTTP.getProtocol()) || address.contains(ProtocolEnum.HTTPS.getProtocol())) {
            this.address = address;
        }else {
            this.address = ProtocolEnum.HTTP.getProtocol() +"://" + address;
        }
        this.accessId = accessId;
        this.secretKey = secretKey;
    }
}
