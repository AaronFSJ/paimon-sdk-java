package com.dreamkey.paimon.application;

import com.dreamkey.paimon.model.bean.PaimonConfig;
import com.dreamkey.paimon.model.bean.Session;

/**
 * 各功能类继承的父类，包含调用所需基础配置信息和 session
 *
 * @author WangHaoquan
 * @date 2022/3/11
 */
public class Paimon {

    protected PaimonConfig config;

    protected Session session;

    public Paimon(PaimonConfig config) {
        this.config = config;
    }

    public Paimon(PaimonConfig config, Session session) {
        this.config = config;
        this.session = session;
    }

}
