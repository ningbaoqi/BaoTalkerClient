package com.dashen.ningbaoqi.factory.presenter.contact;

import android.support.v7.util.DiffUtil;

import com.dashen.ningbaoqi.factory.data.helper.UserHelper;
import com.dashen.ningbaoqi.factory.data.user.ContactDataSource;
import com.dashen.ningbaoqi.factory.data.user.ContactRepository;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.presenter.BaseSourcePresenter;
import com.dashen.ningbaoqi.factory.utils.DiffUiDataCallback;

import java.util.List;

import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;
import project.com.ningbaoqi.factory.data.DataSource;

/**
 * 联系人的 Presenter的实现
 */
public class ContactPresenter extends BaseSourcePresenter<User, User, ContactDataSource, ContactContract.View> implements ContactContract.Presenter, DataSource.SucceedCallback<List<User>> {

    public ContactPresenter(ContactContract.View view) {
        super(new ContactRepository(), view);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据
        UserHelper.refreshContracts();
    }

    /**
     * 数据变更都会通知到这里;保证运行在这里是个子线程
     *
     * @param users
     */
    @Override
    public void onDataLoaded(List<User> users) {
        final ContactContract.View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, users);
    }
}
