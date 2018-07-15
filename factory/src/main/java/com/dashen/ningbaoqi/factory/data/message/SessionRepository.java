package com.dashen.ningbaoqi.factory.data.message;

import android.support.annotation.NonNull;

import com.dashen.ningbaoqi.factory.data.BaseDbRepository;
import com.dashen.ningbaoqi.factory.model.db.Session;
import com.dashen.ningbaoqi.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 最近聊天列表仓库，是对SessionDataSource的实现
 */
public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {
    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
        SQLite.select().from(Session.class).orderBy(Session_Table.modifyAt, true).limit(100).async().queryListResultCallback(this).execute();
    }

    @Override
    protected boolean isRequired(Session session) {
        return true;//所有的会话都需要不需要过滤
    }

    @Override
    protected void insert(Session session) {
        dataList.addFirst(session);//覆写方法，让新的数据添加到链表头部
    }

    /**
     * 覆写数据库回来的方法
     *
     * @param transaction
     * @param tResult
     */
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        super.onListQueryResult(transaction, tResult);
        Collections.reverse(tResult);//反转
    }
}
