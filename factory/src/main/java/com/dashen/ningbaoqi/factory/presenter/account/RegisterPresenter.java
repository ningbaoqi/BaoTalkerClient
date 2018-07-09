package com.dashen.ningbaoqi.factory.presenter.account;

import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.R;
import com.dashen.ningbaoqi.factory.data.helper.AccountHelper;
import com.dashen.ningbaoqi.factory.model.api.account.RegisterModel;
import com.dashen.ningbaoqi.factory.model.db.User;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

import project.com.ningbaoqi.common.Common;
import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.factory.presenter.BasePresenter;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter, DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View view) {
        super(view);
    }

    /**
     * @param phone
     * @param name
     * @param password
     */
    @Override
    public void register(String phone, String name, String password) {
        start();//调用开始；在Start中默认启动了Loading
        RegisterContract.View view = getView();//得到View接口

        if (!checkMobile(phone)) {//手机号不合法就提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {//姓名需要大于两位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {//密码需要大于六位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {//进行网络请求
            RegisterModel model = new RegisterModel(phone, password, name);//构造Model进行请求调用
            AccountHelper.register(model, this);//进行网络请求并设置回调接口
        }
    }

    /**
     * 检查手机号码是否合法
     *
     * @param phone 手机号码
     * @return 合法为true
     */
    @Override
    public boolean checkMobile(String phone) {
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.Constance.REGEX_MOBILE, phone);
    }

    /**
     * 当网络请求成功；注册好了，回送一个用户信息回来;告知界面注册成功
     *
     * @param user
     */
    @Override
    public void onDataLoaded(User user) {
        final RegisterContract.View view = getView();
        if (view == null) {
            return;
        }
        //此时是从网络回送回来的，并不保证处于主线程状态;强制执行在主线程当中
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();//调用主界面注册成功
            }
        });
    }

    /**
     * 网络请求告知注册失败
     *
     * @param strRes
     */
    @Override
    public void onDataNotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
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
