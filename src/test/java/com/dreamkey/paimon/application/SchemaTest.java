package com.dreamkey.paimon.application;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.PaimonBaseTest;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.model.bean.Schema;
import com.dreamkey.paimon.model.bean.SchemaProperty;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.model.query.BaseQuery;
import com.dreamkey.paimon.util.PaimonUtil;
import com.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.List;

/**
 * 操作文档类型测试
 *
 * @author WangHaoquan
 * @date 2022/3/16
 */
public class SchemaTest extends PaimonBaseTest {

    private PaimonSchema paimonSchema;

    @BeforeEach
    void setUp() throws IOException {
        Session session = PaimonUtil.createSession(config);
        paimonSchema = new PaimonSchema(config, session);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/application/schema/addSchemaTest.json"})
    public void addSchemaTest(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        String name = arg.getString("name");
        if (name == null) {
            name = "";
        }
        JSONArray jsonArray = arg.getJSONArray("properties");
        List<SchemaProperty> properties = null;
        if (jsonArray != null) {
            properties = jsonArray.toJavaList(SchemaProperty.class);
        }

        Schema schema = new Schema(name, properties);
        ResponseEntity response = paimonSchema.addSchema(schema);

        System.out.println(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/application/schema/querySchemasTest.json"})
    public void querySchemasTest(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        BaseQuery query = JSONObject.toJavaObject(arg, BaseQuery.class);

        ResponseEntity response = paimonSchema.querySchemas(query);
        System.out.println(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"user400", "user1"})
    public void getSchemaTest(String name) throws IOException {
        ResponseEntity response = paimonSchema.getSchema(name);
        System.out.println(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/application/schema/updateSchemaTest.json"})
    public void updateSchemaTest(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        String name = arg.getString("name");
        if (name == null) {
            name = "";
        }
        JSONArray jsonArray = arg.getJSONArray("properties");
        List<SchemaProperty> properties = null;
        if (jsonArray != null) {
            properties = jsonArray.toJavaList(SchemaProperty.class);
        }

        Schema schema = new Schema(name, properties);

        ResponseEntity response = paimonSchema.updateSchema(schema);
        System.out.println(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"animal"})
    public void rebuildIndexTest(String name) throws IOException {
        ResponseEntity response = paimonSchema.rebuildIndex(name);
        System.out.println(response);
    }

    @ParameterizedTest
    @ValueSource(strings = {"user100"})
    public void deleteSchemaTest(String name) throws IOException {
        ResponseEntity response = paimonSchema.deleteSchema(name);
        System.out.println(response);
    }

}
