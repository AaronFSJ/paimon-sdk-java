package com.dreamkey.paimon.client;

import com.dreamkey.paimon.PaimonBaseTest;
import com.dreamkey.paimon.bean.Pokemon;
import com.dreamkey.paimon.bean.User;
import com.dreamkey.paimon.model.bean.PageInfo;
import com.dreamkey.paimon.model.bean.Schema;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author WangHaoquan
 * @date 2022/3/23
 */
public class SchemaClientTest extends PaimonBaseTest {

    private final SchemaClient schemaClient = new SchemaClient(config);


    @Test
    public void addSchemaTest() throws IOException {
        String name = schemaClient.addSchema(User.class);
        System.out.println(name);
    }

    @Test
    public void querySchemasTest() throws IOException {
        PageInfo<String> pageInfo = schemaClient.querySchemas(1, 10);
        System.out.println(pageInfo);
    }

    @Test
    public void getSchemaTest() throws IOException {
        Schema schema = schemaClient.getSchema(User.class);
        System.out.println(schema);
    }

    @Test
    public void updateSchemaTest() throws IOException {
        schemaClient.updateSchema(User.class);
    }

    @Test
    public void rebuildIndexTest() throws IOException {
        schemaClient.rebuildIndex(User.class);
    }

    @Test
    public void deleteSchemaTest() throws IOException {
        schemaClient.deleteSchema(User.class);
    }
}
