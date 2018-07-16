package com.dashen.ningbaoqi.factory.presenter.group;

import project.com.ningbaoqi.factory.model.Author;
import project.com.ningbaoqi.factory.presenter.BaseContract;

/**
 * 群创建的契约
 */
public interface GroupCreateContract {
    interface Presenter extends BaseContract.Presenter {

        void create(String name, String desc, String picture);

        void changeSelect(ViewModel model, boolean isSelected);//更改选中状态
    }

    interface View extends BaseContract.RecyclerView<Presenter, ViewModel> {
        void onCreateSucceed();//创建成功
    }

    class ViewModel {
        Author author;//用户信息
        boolean isSelected;//是否选中用户的信息
    }
}
