package com.dashen.ningbaoqi.factory.data.user;

import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.data.helper.DBHelper;
import com.dashen.ningbaoqi.factory.model.card.UserCard;
import com.dashen.ningbaoqi.factory.model.db.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserDispatcher implements UserCenter {
    private static UserCenter instance;
    private final Executor executor = Executors.newSingleThreadExecutor();//单线程池：处理卡片一个个的消息进行处理

    public static UserCenter instance() {
        if (instance == null) {
            synchronized (UserDispatcher.class) {
                if (instance == null) {
                    instance = new UserDispatcher();
                }
            }
        }
        return instance;
    }

    @Override
    public void dispatch(UserCard... userCards) {
        if (userCards == null || userCards.length == 0) {
            return;
        }
        executor.execute(new UserCardHandler(userCards));//丢到单线程池中
    }

    /**
     * 线程调度的时候触发run方法
     */
    private class UserCardHandler implements Runnable {

        private final UserCard[] cards;

        UserCardHandler(UserCard[] cards) {
            this.cards = cards;
        }

        @Override
        public void run() {//当被线程调度的时候触发
            List<User> users = new ArrayList<>();
            for (UserCard card : cards) {
                if (card == null || TextUtils.isEmpty(card.getId())) {
                    continue;
                }
                users.add(card.build());
            }
            DBHelper.save(User.class, users.toArray(new User[0]));//进行数据库存储并分发通知，异步的操作
        }
    }
}
