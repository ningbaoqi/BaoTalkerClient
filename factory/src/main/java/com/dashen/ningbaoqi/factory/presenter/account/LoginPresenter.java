package com.dashen.ningbaoqi.factory.presenter.account;

import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.R;
import com.dashen.ningbaoqi.factory.data.helper.AccountHelper;
import com.dashen.ningbaoqi.factory.model.api.account.LoginModel;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.factory.presenter.BasePresenter;

/**
 * 登陆的逻辑实现
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter, DataSource.Callback<User> {


    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getView();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            LoginModel model = new LoginModel(phone, password, Account.getPushId());//尝试拿取pushId
            AccountHelper.login(model, this);
        }
    }

    @Override
    public void onDataLoaded(User user) {
        final LoginContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final LoginContract.View view = getView();
        if (view == null) {
            return;
        }
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
