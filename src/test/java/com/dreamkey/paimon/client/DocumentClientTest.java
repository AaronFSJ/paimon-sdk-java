package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.PaimonBaseTest;
import com.dreamkey.paimon.bean.User;
import com.dreamkey.paimon.model.bean.DocumentLog;
import com.dreamkey.paimon.model.bean.PageInfo;
import com.dreamkey.paimon.model.query.DocumentQuery;
import com.dreamkey.paimon.util.LocalCache;
import com.util.TestUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 5:44 PM 2022/3/21
 * @modified by:
 */
public class DocumentClientTest extends PaimonBaseTest {

    private static Logger logger = Logger.getLogger(DocumentClientTest.class.toString());

    private final DocumentClient documentClient = new DocumentClient(config);


    @ParameterizedTest
    @ValueSource(strings = {"/api/document/addDocumentTest0.json", "/api/document/addDocumentTest1.json"})
    public void addDocumentTest(String str) throws IOException, IllegalAccessException {
        JSONObject arg = TestUtils.getTestArg(str);
        User user = JSONObject.toJavaObject(arg, User.class);
        String documentId = documentClient.addDocument(user);
        logger.log(Level.INFO,"documentId：{0}",documentId);
    }


    @ParameterizedTest
    @ValueSource(strings = {"/api/document/addDocumentTest0.json", "/api/document/addDocumentTest1.json"})
    public void addDocumentStrTest(String str) throws IOException, IllegalAccessException {
        DocumentClient documentClient = new DocumentClient(config);
        JSONObject arg = TestUtils.getTestArg(str);
        String documentId = documentClient.addDocument(arg.toJSONString());
        System.out.println(documentId);
    }

    @ParameterizedTest
    @ValueSource(strings = {"3434343"})
    public void getDocumentTest(String str) throws IOException {
        User user = documentClient.getDocument(str, User.class);
        System.out.println(user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/document/updateDocumentTest.json"})
    public void updateDocumentTest(String str) throws IOException, IllegalAccessException {
        JSONObject arg = TestUtils.getTestArg(str);
        User user = JSONObject.toJavaObject(arg, User.class);
        documentClient.updateDocument(user);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/document/queryDocument.json"})
    public void queryDocument(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        Integer page = arg.getInteger("page");
        Integer size = arg.getInteger("size");
        DocumentQuery query = JSONObject.toJavaObject(arg, DocumentQuery.class);
        PageInfo<User> pageInfo = documentClient.queryDocuments(page, size, query, User.class);
        System.out.println(pageInfo);
    }

    @ParameterizedTest
    @ValueSource(strings = {"7"})
    public void deleteDocumentTest(String str) throws IOException {
        documentClient.deleteDocument(str, User.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"6", "7"})
    public void getDocumentLogTest(String str) throws IOException {
        DocumentLog documentLog = documentClient.getDocumentLog(str, User.class);
        System.out.println(documentLog);
    }
}
