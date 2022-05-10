package com.dreamkey.paimon.application;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.PaimonBaseTest;
import com.dreamkey.paimon.bean.Pokemon;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.enumerate.OperatorEnum;
import com.dreamkey.paimon.model.bean.Document;
import com.dreamkey.paimon.model.bean.Session;
import com.dreamkey.paimon.model.query.DocumentQuery;
import com.dreamkey.paimon.model.query.QueryFilter;
import com.dreamkey.paimon.util.PaimonUtil;
import com.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: Aaron
 * @description:
 * @date: Create in 4:27 PM 2022/3/20
 * @modified by:
 */
public class DocumentTest extends PaimonBaseTest {

    private PaimonDocument paimonDocument;

    @BeforeEach
    void setUp() throws IOException {
        Session session = PaimonUtil.createSession(config);
        paimonDocument = new PaimonDocument(config, session, "Pokemon");
    }

    @ParameterizedTest
    @ValueSource(strings = {"/application/document/createDocumentTest.json"})
    public void addDocumentTest(String str) throws IOException {
        JSONArray array = TestUtils.getTestArray(str);
        // 模拟实际业务传过来是一个普通对象
        List<Pokemon> pokemons = array.toJavaList(Pokemon.class);
        for (Pokemon p : pokemons) {
            String schemaName = PaimonUtil.getSchemaName(p.getClass());
            String pokemonString = JSONObject.toJSONString(p);
            ResponseEntity response = paimonDocument.addDocument(schemaName,p.getId(),pokemonString);
            System.out.println(response);
        }
    }

    @Test
    public void deleteDocument() throws IOException {
        String documentId = "1";
        ResponseEntity response = paimonDocument.deleteDocument(documentId);
        System.out.println(response);
    }


    @ParameterizedTest
    @ValueSource(strings = {"/application/document/updateDocumentTest.json"})
    public void updateDocument(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        // 模拟实际业务传过来是一个普通对象
        Pokemon pokemon = arg.toJavaObject(Pokemon.class);
        String pString = JSONObject.toJSONString(pokemon);
//        Document document = new Document(pokemon.getId(), pString);
        ResponseEntity response = paimonDocument.updateDocument(PaimonUtil.getSchemaName(pokemon.getClass()),pokemon.getId(),pString);
        System.out.println(response);
    }

    @Test
    public void getDocument() throws IOException {
        String documentId = "1";
        Document document = paimonDocument.getDocument(config.getDomain());
        System.out.println(document);
    }

    @Test
    public void queryDocuments() throws IOException {
        DocumentQuery query = new DocumentQuery();
        query.setOffset(0);
        query.setLimit(5);
        List<QueryFilter> filters = new ArrayList<>();
        filters.add(
                QueryFilter.builder()
                        .property("species")
                        .operator(OperatorEnum.EQUAL.getKey())
                        .value("皮卡丘")
                        .build());
        query.setFilters(filters);

        ResponseEntity responseEntity = paimonDocument.queryDocuments(query);
        System.out.println(responseEntity);
    }

    @Test
    public void getDocumentLog() throws IOException {
        String documentId = "1";
        ResponseEntity responseEntity = paimonDocument.getDocumentLog(documentId);
        System.out.println(responseEntity);
    }


}
