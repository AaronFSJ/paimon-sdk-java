package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.application.PaimonDocument;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.annotation.DocId;
import com.dreamkey.paimon.model.bean.*;
import com.dreamkey.paimon.model.query.DocumentQuery;
import com.dreamkey.paimon.util.AssertUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作资产 API
 *
 * @author: Aaron
 * @description:
 * @date: Create in 5:30 PM 2022/3/21
 * @modified by:
 */
public class DocumentClient {

    private final PaimonConfig config;

    public DocumentClient(PaimonConfig config) {
        this.config = config;
    }

    /**
     * 添加资产
     *
     * @param t 泛型，任意已有对应资产类型的资产对象
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
    public <T> String addDocument(T t) throws IOException, IllegalAccessException {
        // 从获取 t 中尝试获取 documentId
        String documentId = this.getDocumentId(t);

        // 封装 document
        Document document = new Document(documentId, JSONObject.toJSONString(t));

        // 创建资产
        String name = t.getClass().getSimpleName();
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, name);
        ResponseEntity response = paimonDocument.addDocument(document);

        String data = response.getData();
        JSONObject dataJson = JSONObject.parseObject(data);

        return dataJson.getString("id");
    }

    /**
     * 更新资产
     *
     * @param t 泛型，任意已有对应资产类型的资产对象
     * @throws IOException
     * @throws IllegalAccessException
     */
    public <T> void updateDocument(T t) throws IOException, IllegalAccessException {
        String documentId = this.getDocumentId(t);
        AssertUtil.hasText(documentId, AssertUtil.hasTextTemplate("documentId"));

        Document document = new Document(documentId, JSONObject.toJSONString(t));

        String name = t.getClass().getSimpleName();
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, name);
        paimonDocument.updateDocument(document);
    }

    /**
     * 分页条件查询资产
     *
     * @param page  当前页码
     * @param size  每页记录数量
     * @param query 过滤条件
     * @param getSession 类信息，指定文档数据转换的类型
     * @return 返回 data 是对应资产的分页列表
     * @throws IOException
     */
    public <T> PageInfo<T> queryDocuments(Integer page, Integer size, DocumentQuery query, Class<T> getSession) throws IOException {
        AssertUtil.notNull(page, AssertUtil.notNullTemplate("page"));
        AssertUtil.notNull(size, AssertUtil.notNullTemplate("size"));
        AssertUtil.isTrue(page > 0 && size > 0, "'page' and 'size' must be greater than 0");

        String name = getSession.getSimpleName();
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, name);

        if (query == null) {
            query = new DocumentQuery();
        }
        query.setOffset((page - 1) * size);
        query.setLimit(size);
        ResponseEntity response = paimonDocument.queryDocuments(query);

        String data = response.getData();
        JSONObject dataJson = JSONObject.parseObject(data);

        Integer total = dataJson.getInteger("total");
        List<Document> documents = dataJson.getJSONArray("documents").toJavaList(Document.class);
        List<T> collection = documents.stream().map(document -> {
            JSONObject content = JSONObject.parseObject(document.getContent());
            return JSONObject.toJavaObject(content, getSession);
        }).collect(Collectors.toList());

        return new PageInfo<>(total,page,size,collection);
    }

    /**
     * 获取资产详情
     *
     * @param documentId 资产 ID
     * @param getSession      类信息，指定文档数据转换的类型
     * @return 具体对象
     * @throws IOException
     */
    public <T> T getDocument(String documentId, Class<T> getSession) throws IOException {
        String name = getSession.getSimpleName();
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, name);
        ResponseEntity response = paimonDocument.getDocument(documentId);

        String data = response.getData();
        Document document = JSONObject.parseObject(data, Document.class);
        String content = document.getContent();
        return JSONObject.toJavaObject(JSONObject.parseObject(content), getSession);
    }

    /**
     * 删除资产
     *
     * @param documentId 资产 ID
     * @param getSession      类信息，用于分析删除的资产所属资产类型
     * @throws IOException
     */
    public <T> void deleteDocument(String documentId, Class<T> getSession) throws IOException {
        String name = getSession.getSimpleName();
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, name);
        paimonDocument.deleteDocument(documentId);
    }

    /**
     * 查询资产变更日志
     *
     * @param documentId 资产 ID
     * @param getSession      类信息，用于分析删除的资产所属资产类型
     * @return
     * @throws IOException
     */
    public <T> DocumentLog getDocumentLog(String documentId, Class<T> getSession) throws IOException {
        String name = getSession.getSimpleName();
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, name);
        ResponseEntity response = paimonDocument.getDocumentLog(documentId);

        String data = response.getData();
        return JSONObject.parseObject(data, DocumentLog.class);
    }

    /**
     * 尝试获取到类中带有 @DocId 注解标注的字段作为资产 ID
     *
     * @param t
     * @return
     * @throws IllegalAccessException
     */
    private <T> String getDocumentId(T t) throws IllegalAccessException {
        // 判断是否有使用 @DocId 注解的字段，有的话作为 资产ID
        Field[] fields = t.getClass().getDeclaredFields();
        String documentId = null;
        for (Field field : fields) {
            field.setAccessible(true);
            DocId docId = field.getAnnotation(DocId.class);
            if (docId != null) {
                documentId = String.valueOf(field.get(t));
                break;
            }
        }
        return documentId;
    }

//    /**
//     * 获取 Content 信息
//     * @param t
//     * @return
//     */
//    private String getContent( T t) {
//        String content = "";
//        if(t.getClass().toString().equals("java.lang.String")) {
//            content = (String) t;
//        }else {
//            content = JSONObject.toJSONString(t);
//        }
//        return content;
//    }

}
