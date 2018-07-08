package com.dashen.ningbaoqi.factory.presenter.account;

import project.com.ningbaoqi.factory.presenter.BaseContract;

public interface RegisterContract {

    interface View extends BaseContract.View<Presenter> {
        void registerSuccess();//注册成功
    }

    interface Presenter extends BaseContract.Presenter {
        void register(String phone, String name, String password);//发起一个注册

        boolean checkMobile(String phone);//检查手机号是否正确
    }
}
