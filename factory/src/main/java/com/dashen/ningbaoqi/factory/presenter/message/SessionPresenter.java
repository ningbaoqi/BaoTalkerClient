package com.dashen.ningbaoqi.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.dashen.ningbaoqi.factory.data.message.SessionDataSource;
import com.dashen.ningbaoqi.factory.data.message.SessionRepository;
import com.dashen.ningbaoqi.factory.model.db.Session;
import com.dashen.ningbaoqi.factory.presenter.BaseSourcePresenter;
import com.dashen.ningbaoqi.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 最近聊天列表的Pressenter
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View> implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    /**
     * 当数据返回的情况下
     *
     * @param sessions
     */
    @Override
    public void onDataLoaded(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null) {
            return;
        }
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);//差异对比
        refreshData(result, sessions);//刷新界面
    }
}
