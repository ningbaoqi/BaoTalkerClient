package com.dashen.ningbaoqi.factory.presenter.contact;

import com.dashen.ningbaoqi.factory.model.card.UserCard;

import project.com.ningbaoqi.factory.presenter.BaseContract;

/**
 * 关注的接口定义
 */
public interface FollowContract {

    interface Presenter extends BaseContract.Presenter {
        void follow(String id);//关注一个人
    }

    interface View extends BaseContract.View<Presenter> {
        void onFollowSucceed(UserCard userCard);
    }
}
