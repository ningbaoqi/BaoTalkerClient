package com.dashen.ningbaoqi.factory.data.user;

import com.dashen.ningbaoqi.factory.data.BaseDbRepository;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.model.db.User_Table;
import com.dashen.ningbaoqi.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import project.com.ningbaoqi.factory.data.DataSource;

/**
 * 联系人仓库
 */
public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {

    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);
        SQLite.select().from(User.class).where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId())).orderBy(User_Table.name, true)
                .limit(100).async().queryListResultCallback(this).execute();
    }

    /**
     * 检查一个User是否是我需要关注的数据
     *
     * @param user User
     * @return true 是我关注的数据
     */
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
