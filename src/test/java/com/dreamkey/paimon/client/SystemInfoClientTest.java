package com.dreamkey.paimon.client;

import com.dreamkey.paimon.PaimonBaseTest;
import com.dreamkey.paimon.model.bean.Change;
import com.dreamkey.paimon.model.bean.DomainParameter;
import com.dreamkey.paimon.model.bean.DomainStatus;
import com.dreamkey.paimon.model.bean.ProposalRule;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author WangHaoquan
 * @date 2022/3/23
 */
public class SystemInfoClientTest extends PaimonBaseTest {

    private final SystemInfoClient systemInfoClient = new SystemInfoClient(config);


    @Test
    public void getDomainStatusTest() throws IOException {
        DomainStatus domainStatus = systemInfoClient.getDomainStatus();
        System.out.println(domainStatus);
    }

    @Test
    public void getDomainParameterTest() throws IOException {
        DomainParameter domainParameter = systemInfoClient.getDomainParameter();
        System.out.println(domainParameter);
    }

    @Test
    public void getProposalRuleTest() throws IOException {
        ProposalRule proposalRule = systemInfoClient.getProposalRule();
        System.out.println(proposalRule);
    }

    @Test
    public void getChangeCommitteeTest() throws IOException {
        Change changeCommittee = systemInfoClient.getChangeCommittee();
        System.out.println(changeCommittee);
    }

    @Test
    public void getChangeMemberTest() throws IOException {
        Change changeMember = systemInfoClient.getChangeMember();
        System.out.println(changeMember);
    }
}
