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
    public static void register(RegisterModel model, final DataSource.Callack<User> callack) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                callack.onDataNotAvailable(com.example.lang.R.string.data_rsp_error_parameters);
            }
        }.start();
    }
}
