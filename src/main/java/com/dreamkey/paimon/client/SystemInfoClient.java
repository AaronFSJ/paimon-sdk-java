package com.dreamkey.paimon.client;

import com.alibaba.fastjson.JSONObject;
import com.dreamkey.paimon.application.PaimonDocument;
import com.dreamkey.paimon.application.PaimonSystem;
import com.dreamkey.paimon.common.ResponseEntity;
import com.dreamkey.paimon.common.StaticConstant;
import com.dreamkey.paimon.model.bean.*;
import com.dreamkey.paimon.util.PaimonUtil;

import java.io.IOException;
import java.util.Objects;

/**
 * 查看一些系统参数
 *
 * @author WangHaoquan
 * @date 2022/3/23
 */
public class SystemInfoClient {

    private final PaimonConfig config;

    public SystemInfoClient(PaimonConfig config) {
        this.config = config;
    }

    /**
     * 查看当前域状态
     *
     * @return
     */
    public DomainStatus getDomainStatus() throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonSystem paimonSystem = new PaimonSystem(config, session);
        ResponseEntity response = paimonSystem.getDomainStatus();

        JSONObject data = JSONObject.parseObject(response.getData());
        return JSONObject.toJavaObject(data, DomainStatus.class);
    }

    /**
     * 查看系统参数
     *
     * @return
     * @throws IOException
     */
    public DomainParameter getDomainParameter() throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, "_domain_parameters");
        Document document = paimonDocument.getDocument(config.getDomain());
        if(Objects.isNull(document)) {
            return null;
        }

        String domain = document.getId();
        String content = document.getContent();
        DomainParameter domainParameter = JSONObject.parseObject(content, DomainParameter.class);
        domainParameter.setDomain(domain);

        return domainParameter;
    }

    /**
     * 查看提议规则
     *
     * @return
     * @throws IOException
     */
    public ProposalRule getProposalRule() throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, "_proposal_rules");
        Document document = paimonDocument.getDocument(config.getDomain());
        if(Objects.isNull(document)) {
            return null;
        }

        String domain = document.getId();
        String content = document.getContent();
        ProposalRule proposalRule = JSONObject.parseObject(content, ProposalRule.class);
        proposalRule.setDomain(domain);

        return proposalRule;
    }

    /**
     * 查看成员涉及变更的规则
     *
     * @return
     * @throws IOException
     */
    public ChangeRule getChangeMember() throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, StaticConstant.CHANGE_MEMBER);
        return getCommonChange(paimonDocument);
    }

    /**
     * 查看委员会涉及变更的规则
     *
     * @return
     * @throws IOException
     */
    public ChangeRule getChangeCommittee() throws IOException {
        Session session = PaimonUtil.getSession(config);
        PaimonDocument paimonDocument = new PaimonDocument(config, session, StaticConstant.CHANGE_COMMITTEE);
        return getCommonChange(paimonDocument);
    }

    /**
     * 查看变更的规则公共方法
     * @param paimonDocument
     * @return
     * @throws IOException
     */
    private ChangeRule getCommonChange(PaimonDocument paimonDocument) throws IOException {
        Document document = paimonDocument.getDocument(config.getDomain());
        if(Objects.isNull(document)) {
            return null;
        }

        String domain = document.getId();
        String content = document.getContent();
        ChangeRule changeRule = JSONObject.parseObject(content, ChangeRule.class);
        changeRule.setDomain(domain);

        return changeRule;
    }

}
