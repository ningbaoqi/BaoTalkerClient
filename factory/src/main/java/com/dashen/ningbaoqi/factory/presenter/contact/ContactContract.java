package com.dashen.ningbaoqi.factory.presenter.contact;

import com.dashen.ningbaoqi.factory.model.db.User;

import project.com.ningbaoqi.factory.presenter.BaseContract;

public interface ContactContract {
    /**
     * 什么都不需要额外定义，开始就是调用start方法即可
     */
    interface Presenter extends BaseContract.Presenter {

    }

    /**
     * 都在基类完成了
     */
    interface View extends BaseContract.RecyclerView<Presenter, User> {

    }
}
