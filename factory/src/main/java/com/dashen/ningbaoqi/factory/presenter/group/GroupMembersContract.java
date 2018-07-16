package com.dashen.ningbaoqi.factory.presenter.group;

import com.dashen.ningbaoqi.factory.model.db.view.MemberUserModel;

import project.com.ningbaoqi.factory.presenter.BaseContract;

/**
 * 群成员契约
 */
public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter {
        void refresh();//刷新
    }

    interface View extends BaseContract.RecyclerView<Presenter, MemberUserModel> {
        String getGroupId();//获取群的Id
    }
}
