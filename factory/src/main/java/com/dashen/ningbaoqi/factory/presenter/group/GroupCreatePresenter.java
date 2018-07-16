package com.dashen.ningbaoqi.factory.presenter.group;

import project.com.ningbaoqi.factory.presenter.BaseRecyclerPresenter;

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View> implements GroupCreateContract.Presenter {
    

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void create(String name, String desc, String picture) {

    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {

    }
}
