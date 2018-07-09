package com.dashen.ningbaoqi.factory.data.helper;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.R;
import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.account.AccountRspModel;
import com.dashen.ningbaoqi.factory.model.api.account.RegisterModel;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.net.NetWork;
import com.dashen.ningbaoqi.factory.net.RemoteService;
import com.dashen.ningbaoqi.factory.persistence.Account;

import project.com.ningbaoqi.factory.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class AccountHelper {

    /**
     * 注册的接口，异步的调用;进行网络请求
     *
     * @param model    传递一个注册的Model进来
     * @param callback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.getRetrofit().create(RemoteService.class);//调用Retrofit对我们的网络请求接口做代理
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);//得到一个Call
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {//进行异步请求
            /**
             * 网络请求成功
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
                RspModel<AccountRspModel> rspModel = response.body();//从返回中得到我们的全局Model，内部是使用的Gson进行解析
                if (rspModel.success()) {//如果是成功的请求
                    AccountRspModel accountRspModel = rspModel.getResult();//拿到实体
                    if (accountRspModel.isBind()) {//如果是绑定状态，是否绑定设备
                        User user = accountRspModel.getUser();
                        // TODO 进行的是数据库写入和缓存绑定；然后返回
                        callback.onDataLoaded(user);
                    } else {//如果没有绑定好设备；要进行绑定设备
                        bindPush(callback);
                    }

                } else {
                    Factory.decodeRspCode(rspModel, callback);//错误解析
                }
            }

            /**
             * 网络请求失败
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备ID进行绑定的操作
     *
     * @param callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        Account.setBind(true);
    }
}
