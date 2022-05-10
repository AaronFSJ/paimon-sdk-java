package com.dreamkey.paimon.common;

/**
 * 归集 SDK 中出现的静态常量
 *
 * @author WangHaoquan
 * @date 2022/3/9
 */
public class StaticConstant {

    /**
     * Paimon 处理请求成功返回的状态码
     */
    public static final Integer ERROR_CODE_OK = 0;

    /**
     * Paimon 处理请求失败返回的状态码
     */
    public static final Integer ERROR_CODE_ERROR = -1;

    /**
     * Http 请求成功状态码
     */
    public static final Integer HTTP_STATUS_OK = 200;

    /**
     * 工作证明参数
     */
    public static final String NONCE = "99999";

    /**
     * 签名方法
     */
    public static final String SIGNATURE_ALGORITHM = "ed25519";

    /**
     * 请求协议前缀
     */
    public static final String REQUEST_PROTOCOL_PREFIX = "http://";

    /**
     * 委员会成员涉及变更的规则常量
     */
    public static final String CHANGE_MEMBER = "_change_member";

    /**
     * 委员会涉及变更的规则常量
     */
    public static final String CHANGE_COMMITTEE = "_change_committee";

    /**
     * 请求路由分组
     */
    public static class UrlGroup {
        public static final String SESSIONS = "/api/v1/sessions/";
        public static final String DOMAINS = "/api/v1/domains/";
    }

}
