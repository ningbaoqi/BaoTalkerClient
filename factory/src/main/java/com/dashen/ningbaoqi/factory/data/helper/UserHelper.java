package com.dashen.ningbaoqi.factory.data.helper;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.R;
import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.user.UserUpdateModel;
import com.dashen.ningbaoqi.factory.model.card.UserCard;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.model.db.User_Table;
import com.dashen.ningbaoqi.factory.net.NetWork;
import com.dashen.ningbaoqi.factory.net.RemoteService;
import com.dashen.ningbaoqi.factory.presenter.contact.FollowPresenter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.List;

import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.utils.CollectionUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserHelper {
    /**
     * 更新用户信息，异步的
     *
     * @param model
     * @param callback
     */
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {//网络请求
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {//如果返回成功
                    UserCard userCard = rspModel.getResult();
                    Factory.getUserCenter().dispatch(userCard);//唤起进行保存的操作
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);//错误情况下进行错误分配
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 搜索的方法
     *
     * @param name     搜索的名字
     * @param callback 搜索完成的回调
     */
    public static Call search(String name, final DataSource.Callback<List<UserCard>> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<List<UserCard>>> call = service.userSearch(name);
        call.enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    callback.onDataLoaded(rspModel.getResult());
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
        return call;//把当前的调度者返回
    }


    /**
     * 关注的网络请求
     *
     * @param id
     * @param callback
     */
    public static void follow(String id, final DataSource.Callback<UserCard> callback) {
        RemoteService service = NetWork.remote();
        Call<RspModel<UserCard>> call = service.userFollow(id);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {//保存用户的信息到本地数据库
                    UserCard userCard = rspModel.getResult();
                    Factory.getUserCenter().dispatch(userCard);//唤起进行保存的操作
                    callback.onDataLoaded(userCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }


    /**
     * 刷新联系人；直接存储到数据库，并通过数据库观察者直接通知进行界面更新；界面更新的时候进行对比差异更新
     */
    public static void refreshContracts() {
        RemoteService service = NetWork.remote();
        service.userContacts().enqueue(new Callback<RspModel<List<UserCard>>>() {
            @Override
            public void onResponse(Call<RspModel<List<UserCard>>> call, Response<RspModel<List<UserCard>>> response) {
                RspModel<List<UserCard>> rspModel = response.body();
                if (rspModel.success()) {
                    List<UserCard> cards = rspModel.getResult();
                    if (cards == null || cards.size() == 0) {
                        return;
                    }
                    UserCard[] cards1 = CollectionUtil.toArray(cards, UserCard.class);
                    Factory.getUserCenter().dispatch(cards1);
                } else {
                    Factory.decodeRspCode(rspModel, null);
                }
            }

            @Override
            public void onFailure(Call<RspModel<List<UserCard>>> call, Throwable t) {
                // do nothing
            }
        });
    }

    /**
     * 搜索一个用户，优先本地缓存，然后再从网络拉取
     *
     * @param id
     * @return
     */
    public static User search(String id) {
        User user = findFromLocal(id);
        if (user == null) {
            return findFromNet(id);
        }
        return user;
    }

    /**
     * 搜索一个用户，优先网络查询；没有然后从本地缓存拉取
     *
     * @param id
     * @return
     */
    public static User searchFirstOfNet(String id) {
        User user = findFromNet(id);
        if (user == null) {
            return findFromLocal(id);
        }
        return user;
    }

    /**
     * 从本地查询一个用户的信息
     *
     * @param id
     * @return
     */
    public static User findFromLocal(String id) {
        return SQLite.select().from(User.class).where(User_Table.id.eq(id)).querySingle();
    }

    /**
     * 从网络中查询一个用户的信息
     *
     * @param id
     * @return
     */
    public static User findFromNet(String id) {
        RemoteService service = NetWork.remote();
        try {
            Response<RspModel<UserCard>> response = service.userFind(id).execute();//同步的请求
            UserCard card = response.body().getResult();
            if (card != null) {
                User user = card.build();
                Factory.getUserCenter().dispatch(card);
                return user;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
