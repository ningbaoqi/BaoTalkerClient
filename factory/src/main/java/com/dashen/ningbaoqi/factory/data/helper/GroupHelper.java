package com.dashen.ningbaoqi.factory.data.helper;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.R;
import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.group.GroupCreateModel;
import com.dashen.ningbaoqi.factory.model.card.GroupCard;
import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.Group_Table;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.net.NetWork;
import com.dashen.ningbaoqi.factory.net.RemoteService;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;

import project.com.ningbaoqi.factory.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupHelper {

    /**
     * 查询群的信息。先本地后网络
     *
     * @param groupId
     * @return
     */
    public static Group find(String groupId) {
        Group group = findFromLocal(groupId);
        if (group == null) {
            group = findFromNet(groupId);
        }
        return group;
    }

    /**
     * 从本地找群
     *
     * @param groupId
     * @return
     */
    public static Group findFromLocal(String groupId) {
        return SQLite.select().from(Group.class).where(Group_Table.id.eq(groupId)).querySingle();
    }

    /**
     * 从网络找群
     *
     * @param groupId
     * @return
     */
    public static Group findFromNet(String groupId) {
        RemoteService service = NetWork.remote();
        try {
            Response<RspModel<GroupCard>> response = service.groupFind(groupId).execute();
            GroupCard card = response.body().getResult();
            if (card != null) {
                Factory.getGroupCenter().dispatch(card);//数据库存储并通知
                User user = UserHelper.search(card.getOwnerId());
                if (user != null) {
                    return card.build(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进行群的创建
     *
     * @param model
     * @param callback
     */
    public static void create(GroupCreateModel model, final DataSource.Callback<GroupCard> callback) {
        RemoteService service = NetWork.remote();
        service.groupCreate(model).enqueue(new Callback<RspModel<GroupCard>>() {
            @Override
            public void onResponse(Call<RspModel<GroupCard>> call, Response<RspModel<GroupCard>> response) {
                RspModel<GroupCard> rspModel = response.body();
                if (rspModel.success()) {
                    GroupCard groupCard = rspModel.getResult();
                    Factory.getGroupCenter().dispatch(groupCard);
                    callback.onDataLoaded(groupCard);
                } else {
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<GroupCard>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
