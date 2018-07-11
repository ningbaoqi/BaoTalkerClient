package com.dashen.ningbaoqi.factory.presenter.contact;

import com.dashen.ningbaoqi.factory.model.db.User;

import project.com.ningbaoqi.factory.presenter.BaseContract;

public interface PersonalContract {
    interface Presenter extends BaseContract.Presenter {
        User getUserPersonal();//获取用户信息
    }

    interface View extends BaseContract.View<Presenter> {
        String getUserId();

        void onLoadDone(User user);//加载数据完成

        void allowSayHello(boolean isAllow);//是否发起聊天

        void setFollowStatus(boolean isFollow);//设置关注状态
    }
}
