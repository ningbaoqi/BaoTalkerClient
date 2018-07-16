package com.dashen.ningbaoqi.factory.presenter.group;

import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.User;

import project.com.ningbaoqi.factory.presenter.BaseContract;

/**
 * 我的群列表契约
 */
public interface GroupsContract {
    /**
     * 什么都不需要额外定义，开始就是调用start方法即可
     */
    interface Presenter extends BaseContract.Presenter {

    }

    /**
     * 都在基类完成了
     */
    interface View extends BaseContract.RecyclerView<Presenter, Group> {

    }
}
