package com.dashen.ningbaoqi.factory.presenter.contact;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.dashen.ningbaoqi.factory.data.helper.UserHelper;
import com.dashen.ningbaoqi.factory.model.card.UserCard;
import com.dashen.ningbaoqi.factory.model.db.AppDatabase;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.model.db.User_Table;
import com.dashen.ningbaoqi.factory.persistence.Account;
import com.dashen.ningbaoqi.factory.utils.DiffUiDataCallback;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.factory.presenter.BasePresenter;

/**
 * 联系人的 Presenter的实现
 */
public class ContactPresenter extends BasePresenter<ContactContract.View> implements ContactContract.Presenter {

    public ContactPresenter(ContactContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        // TODO 加载数据
        SQLite.select().from(User.class)//加载本地数据库数据
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapyer().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                })
                .execute();

        //加载网络数据
        UserHelper.refreshContracts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(int strRes) {
                // do nothing
            }

            @Override
            public void onDataLoaded(final List<UserCard> userCards) {// 保存数据到数据库
                final List<User> users = new ArrayList<>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }

                DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                definition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class).saveAll(users);
                    }
                }).build().execute();

                //网络的数据往往是新的
                List<User> olds = getView().getRecyclerAdapyer().getItems();
                diff(users, olds);
            }
        });

        // TODO 问题：关注后虽然存储了数据库但是没有刷新联系人  如果刷新数据库或者从网络刷新最终刷新的是全局刷新  本地刷新和网络刷新都是异步的，但是在添加到界面的时候会后冲突 导致数据显示异常 如何识别已经在数据库中有这样的数据了
    }

    /**
     * 计算
     *
     * @param newUsers
     * @param oldUsers
     */
    private void diff(List<User> newUsers, List<User> oldUsers) {
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldUsers, newUsers);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        getView().getRecyclerAdapyer().replace(newUsers);//在对比完成后进行数据的赋值
        result.dispatchUpdatesTo(getView().getRecyclerAdapyer());//尝试刷新
        getView().onAdapterDataChanged();
    }
}
