package com.dashen.ningbaoqi.factory.presenter.account;

import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.data.helper.AccountHelper;
import com.dashen.ningbaoqi.factory.model.api.RegisterModel;

import java.util.regex.Pattern;

import project.com.ningbaoqi.common.Common;
import project.com.ningbaoqi.factory.presenter.BasePresenter;

public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
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
        if (!checkMobile(phone)) {//手机号不合法就提示

        } else if (name.length() < 2) {//姓名需要大于两位

        } else if (password.length() < 6) {//密码需要大于六位

        } else {//进行网络请求
            RegisterModel model = new RegisterModel(phone, password, name);//构造Model进行请求调用
            AccountHelper.register(model);
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
}
