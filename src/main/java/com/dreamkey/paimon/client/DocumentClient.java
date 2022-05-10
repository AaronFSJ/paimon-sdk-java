package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.application.PaimonDocument;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.model.bean.*;
import com.dreamkey.paimon.model.query.DocumentQuery;
import com.dreamkey.paimon.util.AssertUtil;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 操作资产实例 API
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
     * 添加资产实例，以用户传入的实体添加资产实例
     *
     * @param t 泛型，任意已有对应资产类型的资产对象
     * @return
     * @throws IOException
     * @throws IllegalAccessException
     */
    public <T> String addDocument(T t) throws IOException, IllegalAccessException {
        // 从获取 t 中尝试获取 documentId
        String documentId = PaimonUtil.getDocumentId(t);
        // 获取 schemaName
        String schemaName = PaimonUtil.getSchemaName(t.getClass());
        // 创建资产
        String content = JSONObject.toJSONString(t);
        return addDocument(schemaName,documentId,content);
    }

    /**
     * 添加资产
     * @param schemaName schemaName
     * @param documentId
     * @param content
     * @return
     * @throws IOException
     */
    public String addDocument(String schemaName,String documentId,String content) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, schemaName);
        ResponseEntity response = paimonDocument.addDocument(schemaName,documentId,content);

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
        // 从获取 t 中尝试获取 documentId
        String documentId = PaimonUtil.getDocumentId(t);
        AssertUtil.hasText(documentId, AssertUtil.hasTextTemplate("documentId"));
        // 获取 schemaName
        String schemaName = PaimonUtil.getSchemaName(t.getClass());
        String content = JSONObject.toJSONString(t);
        this.updateDocument(schemaName,documentId,content);
    }


    /**
     * 更新资产
     * @param schemaName schemaName
     * @param documentId
     * @param content
     * @return
     * @throws IOException
     */
    public void updateDocument(String schemaName,String documentId,String content) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, schemaName);
        paimonDocument.addDocument(schemaName,documentId,content);
    }

    /**
     * 分页条件查询资产，返回指定类型分页结果集
     *
     * @param page  当前页码
     * @param size  每页记录数量
     * @param query 过滤条件
     * @param clazz 类信息，指定文档数据转换的类型
     * @return 返回 data 是对应资产的分页列表
     * @throws IOException
     */
    public <T> PageInfo<T> queryDocuments(Integer page, Integer size, DocumentQuery query, Class<T> clazz) throws IOException {
        String schemaName = PaimonUtil.getSchemaName(clazz);
        String data = queryDocuments(page,size,schemaName,query);
        JSONObject dataJson = JSONObject.parseObject(data);

        Integer total = dataJson.getInteger("total");
        List<Document> documents = dataJson.getJSONArray("documents").toJavaList(Document.class);
        List<T> collection = documents.stream().map(document -> {
            JSONObject content = JSONObject.parseObject(document.getContent());
            return JSONObject.toJavaObject(content, clazz);
        }).collect(Collectors.toList());

        return new PageInfo<>(total,page,size,collection);
    }

    /**
     * 分页条件查询资产，返回分页结果集字符串
     * @param page
     * @param size
     * @param schemaName
     * @param query
     * @return
     * @throws IOException
     */
    public String queryDocuments(Integer page, Integer size, String schemaName,DocumentQuery query) throws IOException {
        AssertUtil.notNull(page, AssertUtil.notNullTemplate("page"));
        AssertUtil.notNull(size, AssertUtil.notNullTemplate("size"));
        AssertUtil.isTrue(page > 0 && size > 0, "'page' and 'size' must be greater than 0");

        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, schemaName);

        if (query == null) {
            query = new DocumentQuery();
        }
        query.setOffset((page - 1) * size);
        query.setLimit(size);
        ResponseEntity response = paimonDocument.queryDocuments(query);

        String data = response.getData();
        return data;
    }

    public Boolean checkDocumentExists (String documentId, String schemaName) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, schemaName);
        return paimonDocument.checkDocumentExists(documentId);
    }



    /**
     * 获取资产详情，返回指定类型对象
     *
     * @param documentId 资产 ID
     * @param clazz      类信息，指定文档数据转换的类型
     * @return 具体对象
     * @throws IOException
     */
    public <T> T getDocument(String documentId, Class<T> clazz) throws IOException {
        String name = PaimonUtil.getSchemaName(clazz);
        Document document = getDocument(documentId,name);
        if(Objects.isNull(document)) {
            return null;
        }
        String content = document.getContent();
        return JSONObject.toJavaObject(JSONObject.parseObject(content), clazz);
    }


    /**
     * 获取资产详情，返回 Document
     *
     * @param documentId 资产 ID
     * @param schemaName
     * @return 具体对象
     * @throws IOException
     */
    public Document getDocument(String documentId, String schemaName) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, schemaName);
        return paimonDocument.getDocument(documentId);
    }

    /**
     * 删除资产，通过类 class 获取 schemaName 删除
     *
     * @param documentId 资产 ID
     * @param clazz      类信息，用于分析删除的资产所属资产类型
     * @throws IOException
     */
    public <T> void deleteDocument(String documentId, Class<T> clazz) throws IOException {
        String name = PaimonUtil.getSchemaName(clazz);
        deleteDocument(documentId,name);
    }

    /**
     * 删除资产，通过 schemaName 删除资产
     *
     * @param documentId 资产 ID
     * @param schemaName
     * @throws IOException
     */
    public void deleteDocument(String documentId, String schemaName) throws IOException {
        AssertUtil.hasText(documentId, AssertUtil.hasTextTemplate("documentId"));
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, schemaName);
        paimonDocument.deleteDocument(documentId);
    }

    /**
     * 查询资产实例变更日志，通过类 class 获取 schemaName 实现资产变更
     *
     * @param documentId 资产 ID
     * @param clazz      类信息，用于分析删除的资产所属资产类型
     * @return
     * @throws IOException
     */
    public <T> DocumentLog getDocumentLog(String documentId, Class<T> clazz) throws IOException {
        String name = PaimonUtil.getSchemaName(clazz);
        return getDocumentLog(documentId,name);
    }

    /**
     * 查询资产实例变更日志
     *
     * @param documentId 资产 ID
     * @param schemaName
     * @return
     * @throws IOException
     */
    public DocumentLog getDocumentLog(String documentId, String schemaName) throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, schemaName);
        ResponseEntity response = paimonDocument.getDocumentLog(documentId);

        String data = response.getData();
        return JSONObject.parseObject(data, DocumentLog.class);
    }




}
