package com.dreamkey.paimon.application;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.common.enumerate.RequestMethod;
import com.dreamkey.paimon.model.bean.Document;
import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.model.query.DocumentQuery;
import com.dreamkey.paimon.util.OkHttpUtil;
import com.dreamkey.paimon.util.PaimonUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * 操作资产（document）
 *
 * @author WangHaoquan
 * @date 2022/3/9
 */
public class PaimonDocument extends Paimon {

    private final String documentOriginPath = StaticConstant.UrlGroup.DOMAINS + config.getDomain() + "/schemas/{schemaName}/docs/";

    private final String documentPath;

    private final String requestPath;

    private final String schemaName;

    public PaimonDocument(PaimonConfig config, Session session, String schemaName) {
        super(config, session);
        this.schemaName = schemaName;
        this.documentPath = documentOriginPath.replace("{schemaName}", schemaName);
        this.requestPath = config.getAddress() + documentPath;
    }


    public ResponseEntity addDocument(String schemaName, String documentId,String content) throws IOException{
        Document document = new Document(documentId,content);
        String requestBody = JSONObject.toJSONString(document);
        // 组装 headers
        Map<String, String> headers = PaimonUtil.createHeader(RequestMethod.POST, documentPath, requestBody, config, this.session.getSession());
        // 发起同步 Http 请求
        return OkHttpUtil.builder().doPost(requestPath, headers, requestBody);
    }

    /**
     * 删除资产
     *
     * @param documentId
     * @return
     * @throws IOException
     */
    public ResponseEntity deleteDocument(String documentId) throws IOException {
        String headerSignUrl = documentPath + documentId;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.DELETE, headerSignUrl, "", config, this.session.getSession());

        String url = requestPath + documentId;
        return OkHttpUtil.builder().doDelete(url, header, "");
    }

    /**
     * 更新资产
     *
     * @param document
     * @return
     * @throws IOException
     */
//    public ResponseEntity updateDocument(Document document) throws IOException {
//        String headerSignUrl = documentPath + document.getId();
//        String requestBody = JSONObject.toJSONString(document);
//        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.PUT, headerSignUrl, requestBody, config, this.session.getSession());
//
//        String url = requestPath + document.getId();
//        return OkHttpUtil.builder().doPut(url, header, requestBody);
//    }

    /**
     * 更新资产
     * @param schemaName
     * @param documentId
     * @param content
     * @return
     * @throws IOException
     */
    public ResponseEntity updateDocument(String schemaName,String documentId,String content) throws IOException{
        String headerSignUrl = documentPath + documentId;
        Document document = new Document(documentId,content);
        String requestBody = JSONObject.toJSONString(document);
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.PUT, headerSignUrl, requestBody, config, this.session.getSession());

        String url = requestPath + document.getId();
        return OkHttpUtil.builder().doPut(url, header, requestBody);
    }


    public boolean checkDocumentExists(String documentId)  throws IOException {
        String headerSignUrl = documentPath + documentId;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.HEAD, headerSignUrl, "", config, session.getSession());

        String url = requestPath + documentId;
        ResponseEntity response = OkHttpUtil.builder().doHead(url, header, null);
        if(StaticConstant.ERROR_CODE_OK .equals(response.getErrorCode())) {
            return true;
        }
        return false;
    }

    /**
     * 获取资产详情
     *
     * @param documentId
     * @return
     * @throws IOException
     */
    public Document getDocument(String documentId) throws IOException {
        if(!checkDocumentExists(documentId)){
            return null;
        }

        String headerSignUrl = documentPath + documentId;
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.GET, headerSignUrl, "", config, session.getSession());

        String url = requestPath + documentId;
        ResponseEntity response = OkHttpUtil.builder().doGet(url, header, null);
        String data = response.getData();
        Document document = StringUtils.isEmpty(data)? null : JSONObject.parseObject(data, Document.class);
        return document;
    }

    /**
     * 分页条件查询资产列表
     *
     * @param query
     * @return
     * @throws IOException
     */
    public ResponseEntity queryDocuments(DocumentQuery query) throws IOException {
        String uri = StaticConstant.UrlGroup.DOMAINS + config.getDomain() + "/queries/schemas/" + schemaName + "/docs/";

        JSONObject queryJson = (JSONObject) JSONObject.toJSON(query);
        String requestBody = JSONObject.toJSONString(queryJson);

        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.POST, uri, requestBody, config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doPost(url, header, requestBody);
    }

    /**
     * 获取资产的更新日志记录
     *
     * @param documentId
     * @return
     * @throws IOException
     */
    public ResponseEntity getDocumentLog(String documentId) throws IOException {
        String uri = documentPath + documentId + "/logs/";
        Map<String, String> header = PaimonUtil.createHeader(RequestMethod.GET, uri, "", config, session.getSession());

        String url = config.getAddress() + uri;
        return OkHttpUtil.builder().doGet(url, header, null);
    }


}
