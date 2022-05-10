package com.dreamkey.paimon.application;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.common.enumerate.RequestMethod;
import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Schema;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.model.query.BaseQuery;
import com.dreamkey.paimon.util.OkHttpUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;
import java.util.Map;

/**
 * 操作资产类型（schema）
 *
 * @author WangHaoquan
 * @date 2022/3/9
 */
public class PaimonSchema extends Paimon {

    private final String schemaPath = StaticConstant.UrlGroup.DOMAINS + config.getDomain() + "/schemas/";

    public PaimonSchema(PaimonConfig config, Session session) {
        super(config, session);
    }

    /**
     * 创建资产类型
     *
     * @param schema
     * @return
     * @throws IOException
     */
    public ResponseEntity addSchema(Schema schema) throws IOException {
        // 创建 uri 和请求体变量
        String uri = schemaPath + schema.getName();
        String body = JSONObject.toJSONString(schema.getProperties());
        // 组装 headers
        Map<String, String> headers = PaimonUtil.createHeader(RequestMethod.POST, uri, body, config, this.session.getSession());
        // 创建 url
        String url = config.getAddress() + uri;
        // 发起同步 Http 请求
        return OkHttpUtil.builder().doPost(url, headers, body);
    }

    /**
     * 分页查询资产类型列表
     *
     * @param query 查询过滤条件
     * @return
     * @throws IOException
     */
    public ResponseEntity querySchemas(BaseQuery query) throws IOException {
        String uri = schemaPath;
        String body = JSON.toJSONString(query);
        Map<String, String> headers = PaimonUtil.createHeader(RequestMethod.POST, uri, body, config, this.session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPost(url, headers, body);
    }

    /**
     * 检查 Schema 是否存在
     * @param schemaName
     * @return
     * @throws IOException
     */
    public boolean checkSchemaExists(String schemaName)  throws IOException {
        String uri = schemaPath + schemaName;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.HEAD, uri, "", config, session.getSession());

        String url = config.getAddress() + uri;
        ResponseEntity response = OkHttpUtil.builder().doHead(url, header, null);
        if(StaticConstant.ERROR_CODE_OK .equals(response.getErrorCode())) {
            return true;
        }
        return false;
    }

    /**
     * 获取单个资产详情
     *
     * @param name 资产类型名称
     * @return
     */
    public ResponseEntity getSchema(String name) throws IOException {
        String uri = schemaPath + name;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.GET, uri, "", config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doGet(url, header, null);
    }

    /**
     * 更新资产类型
     *
     * @param schema
     * @return
     * @throws IOException
     */
    public ResponseEntity updateSchema(Schema schema) throws IOException {
        String uri = schemaPath + schema.getName();
        String requestBody = JSONObject.toJSONString(schema.getProperties());
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.PUT, uri, requestBody, config, this.session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPut(url, header, requestBody);
    }

    /**
     * 获取单个 schema 详情
     *
     * @param name schema 名称
     * @return
     */
    public ResponseEntity deleteSchema(String name) throws IOException {
        String uri = schemaPath + name;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.DELETE, uri, "", config, this.session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doDelete(url, header, "");
    }

    /**
     * 重建文档类型字段索引（使用 update 方法更新字段是否索引的状态后，需要调用此方法重建生效）
     *
     * @param name 文档类型名称
     * @returns
     */
    public ResponseEntity rebuildIndex(String name) throws IOException {
        String uri = schemaPath + name + "/index/";
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.POST, uri, "", config, this.session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPost(url, header, "");
    }


}
