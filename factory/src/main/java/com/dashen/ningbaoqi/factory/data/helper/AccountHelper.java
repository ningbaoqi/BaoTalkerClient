package com.dashen.ningbaoqi.factory.data.helper;

import com.dashen.ningbaoqi.factory.model.api.account.RegisterModel;
import com.dashen.ningbaoqi.factory.model.db.User;

import project.com.ningbaoqi.factory.data.DataSource;

/**
 *
 */
public class AccountHelper {

    /**
     * 注册的接口，异步的调用
     *
     * @param model   传递一个注册的Model进来
     * @param callack 成功与失败的接口回送
     */
    public static void register(RegisterModel model, DataSource.Callack<User> callack) {

    }
}
