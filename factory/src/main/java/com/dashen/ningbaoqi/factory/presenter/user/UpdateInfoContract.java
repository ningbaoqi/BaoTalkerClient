package com.dashen.ningbaoqi.factory.presenter.user;

import project.com.ningbaoqi.factory.presenter.BaseContract;

/**
 * 更新用户信息的基本的契约
 */
public class UpdateInfoContract {
    public interface Presenter extends BaseContract.Presenter {
        void update(String photoFilePath, String desc, boolean isMan);//更新
    }

    public interface View extends BaseContract.View<Presenter> {
        void updateSucceed();//回调成功
    }
}
