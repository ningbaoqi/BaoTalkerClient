package com.dashen.ningbaoqi.factory.presenter.group;

import android.support.v7.util.DiffUtil;

import com.dashen.ningbaoqi.factory.data.group.GroupsDataSource;
import com.dashen.ningbaoqi.factory.data.group.GroupsRepository;
import com.dashen.ningbaoqi.factory.data.helper.GroupHelper;
import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.presenter.BaseSourcePresenter;
import com.dashen.ningbaoqi.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 我的群组Presenter
 */
public class GroupsPresenter extends BaseSourcePresenter<Group, Group, GroupsDataSource, GroupsContract.View> implements GroupsContract.Presenter {

    public GroupsPresenter(GroupsContract.View view) {
        super(new GroupsRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        GroupHelper.refreshGroups();//加载网络数据，以后可以优化到下拉刷新中，只有用户下拉进行网络请求刷新
    }

    @Override
    public void onDataLoaded(List<Group> groups) {
        final GroupsContract.View view = getView();
        if (view == null) {
            return;
        }
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, groups);
    }
}
