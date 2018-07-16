package com.dashen.ningbaoqi.factory.presenter.group;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.data.helper.GroupHelper;
import com.dashen.ningbaoqi.factory.data.helper.UserHelper;
import com.dashen.ningbaoqi.factory.model.db.view.MemberUserModel;
import com.dashen.ningbaoqi.factory.model.db.view.UserSampleModel;

import java.util.ArrayList;
import java.util.List;

import project.com.ningbaoqi.factory.presenter.BaseRecyclerPresenter;

public class GroupMembersPresenter extends BaseRecyclerPresenter<MemberUserModel, GroupMembersContract.View> implements GroupMembersContract.Presenter {

    public GroupMembersPresenter(GroupMembersContract.View view) {
        super(view);
    }

    @Override
    public void refresh() {
        start();//显示Loading
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null) {
                return;
            }
            String groupId = view.getGroupId();
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);//-1表示查询所有
            refreshData(models);
        }
    };
}
