package com.dashen.ningbaoqi.factory.data.user;

import android.support.annotation.NonNull;

import com.dashen.ningbaoqi.factory.data.helper.DBHelper;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.model.db.User_Table;
import com.dashen.ningbaoqi.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project.com.ningbaoqi.factory.data.DataSource;

/**
 * 联系人仓库
 */
public class ContractRepository implements ContractDataSource, QueryTransaction.QueryResultListCallback<User>, DBHelper.ChangedListener<User> {
    private DataSource.SucceedCallback<List<User>> callback;
    private final Set<User> users = new HashSet<>();

    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        this.callback = callback;
        DBHelper.addChangedListener(User.class, this);//对数据辅助工具类添加一个数据更新的监听
        SQLite.select().from(User.class).where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId())).orderBy(User_Table.name, true)
                .limit(100).async().queryListResultCallback(this).execute();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DBHelper.removeChangedListener(User.class, this);
    }

    /**
     * 数据库加载数据成功
     *
     * @param transaction
     * @param tResult
     */
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
        users.addAll(tResult);//添加到自己当前的缓冲区
        if (callback != null) {
            callback.onDataLoaded(tResult);
        }
    }

    /**
     * 当数据库数据变更的操作
     *
     * @param datas
     */
    @Override
    public void onDataSave(User... datas) {
        for (User data : datas) {
            if (isRequired(data)) {//如果是我想要的数据

            }
        }
    }

    /**
     * 当数据库数据删除的操作
     *
     * @param datas
     */
    @Override
    public void onDataDelete(User... datas) {

    }

    /**
     * 检查一个User是否是我需要关注的数据
     *
     * @param user User
     * @return true 是我关注的数据
     */
    private boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }

    /**
     * @param user
     */
    private void insertOrUpdate(User user) {
        
    }
}
