package com.dashen.ningbaoqi.factory.presenter.account;

import project.com.ningbaoqi.factory.presenter.BasePresenter;

/**
 * 登陆的逻辑实现
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {


    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {

    }
}
