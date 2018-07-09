package com.dashen.ningbaoqi.factory.presenter.account;

import project.com.ningbaoqi.factory.presenter.BaseContract;

public interface LoginContract {
    interface View extends BaseContract.View<Presenter> {
        void loginSuccess();//登陆成功
    }

    interface Presenter extends BaseContract.Presenter {
        void login(String phone, String password);//发起一个登陆
    }
}
