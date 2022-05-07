package com.dreamkey.paimon.client;

import com.dreamkey.paimon.PaimonBaseTest;
import com.dreamkey.paimon.model.bean.Block;
import com.dreamkey.paimon.model.bean.PageInfo;
import com.dreamkey.paimon.model.bean.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

/**
 * @author WangHaoquan
 * @date 2022/3/23
 */
public class BlockClientTest extends PaimonBaseTest {

    private final BlockClient blockClient = new BlockClient(config);

    @Test
    public void queryBlockTest() throws IOException {
        PageInfo<String> pageInfo = blockClient.queryBlock(1, 5);
        System.out.println(pageInfo);
    }

    @ParameterizedTest
    @ValueSource(strings = {"4d724f1996772507cf4bda22cbe586c9d41c2ac454de9e739c7a04510f36f157", "60d025a9532e611630cbd5a49b4faeb4b7153f6a204e6e386b1f6355837ee537", "22390d9bdc898018d8e73f788f555ded349f50d041ff3e576008385d2c3ad44f"})
    public void getBlockTest(String str) throws IOException {
        Block block = blockClient.getBlock(str);
        System.out.println(block);
    }

    @Test
    public void queryTransactionTest() throws IOException {
        PageInfo<String> pageInfo = blockClient.queryTransaction(
                "4d724f1996772507cf4bda22cbe586c9d41c2ac454de9e739c7a04510f36f157",
                1, 5
        );
        System.out.println(pageInfo);
    }

    @Test
    public void getTransactionTest() throws IOException {
        Transaction transaction = blockClient.getTransaction(
                "4d724f1996772507cf4bda22cbe586c9d41c2ac454de9e739c7a04510f36f157",
                "1"
        );
        System.out.println(transaction);
    }

}
