package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.PaimonBaseTest;
import com.dreamkey.paimon.model.bean.Contract;
import com.dreamkey.paimon.model.bean.ContractDetail;
import com.dreamkey.paimon.model.bean.PageInfo;
import com.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

/**
 * @author WangHaoquan
 * @date 2022/3/23
 */
public class ContractClientTest extends PaimonBaseTest {

    private final ContractClient contractClient = new ContractClient(config);


    @ParameterizedTest
    @ValueSource(strings = {"/api/contract/deployContractTest.json"})
    public void deployContractTest(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        String name = arg.getString("name");
        String content = arg.getString("content");
        contractClient.deployContract(name, content);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/contract/callContractTest.json"})
    public void callContractTest(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        String name = arg.getString("name");
        JSONArray jsonArray = arg.getJSONArray("parameters");
        String[] parameters = jsonArray.toArray(new String[0]);
        contractClient.callContract(name, "234","常威","男","29");
    }

    @ParameterizedTest
    @ValueSource(strings = {"add_user"})
    public void getContractTest(String name) throws IOException {
        ContractDetail contractDetail = contractClient.getContractDetail(name);
        System.out.println(contractDetail);
    }

    @Test
    public void queryContractTest() throws IOException {
        PageInfo<Contract> pageInfo = contractClient.queryContract(1, 5);
        System.out.println(pageInfo);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/contract/traceContractTest.json"})
    public void traceContractTest(String str) throws IOException {
        JSONObject arg = TestUtils.getTestArg(str);
        String name = arg.getString("name");
        Boolean enable = arg.getBoolean("enable");
        contractClient.traceContract(name, enable);
    }

    @ParameterizedTest
    @ValueSource(strings = {"add_user"})
    public void cancelContract(String str) throws IOException {
        contractClient.cancelContract(str);
    }
}
