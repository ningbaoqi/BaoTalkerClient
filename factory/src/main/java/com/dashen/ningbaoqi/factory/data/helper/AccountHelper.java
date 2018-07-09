package com.dashen.ningbaoqi.factory.data.helper;

import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.R;
import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.account.AccountRspModel;
import com.dashen.ningbaoqi.factory.model.api.account.LoginModel;
import com.dashen.ningbaoqi.factory.model.api.account.RegisterModel;
import com.dashen.ningbaoqi.factory.model.db.AppDatabase;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.net.NetWork;
import com.dashen.ningbaoqi.factory.net.RemoteService;
import com.dashen.ningbaoqi.factory.persistence.Account;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import project.com.ningbaoqi.factory.data.DataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求执行类
 */
public class AccountHelper {

    /**
     * 注册的接口，异步的调用;进行网络请求
     *
     * @param model    传递一个注册的Model进来
     * @param callback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.remote();//调用Retrofit对我们的网络请求接口做代理
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);//得到一个Call
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 登陆的调用
     *
     * @param model    登陆的model
     * @param callback 成功与失败的接口回调
     */
    public static void login(final LoginModel model, final DataSource.Callback<User> callback) {
        RemoteService service = NetWork.remote();//调用Retrofit对我们的网络请求接口做代理
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);//得到一个Call
        call.enqueue(new AccountRspCallback(callback));
    }


    /**
     * 对设备ID进行绑定的操作
     *
     * @param callback
     */
    public static void bindPush(final DataSource.Callback<User> callback) {
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {//如果pushId为空则返回
            return;
        }
        RemoteService service = NetWork.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));
    }

    /**
     * 请求的回调部分封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {
        final DataSource.Callback<User> callback;

        AccountRspCallback(DataSource.Callback<User> callback) {
            this.callback = callback;
        }

        /**
         * 网络请求成功
         *
         * @param call
         * @param response
         */
        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call, Response<RspModel<AccountRspModel>> response) {
            RspModel<AccountRspModel> rspModel = response.body();//从返回中得到我们的全局Model，内部是使用的Gson进行解析
            if (rspModel.success()) {//如果是成功的请求
                AccountRspModel accountRspModel = rspModel.getResult();//拿到实体
                final User user = accountRspModel.getUser();
                user.save();//第一种;直接保存
                            /*FlowManager.getModelAdapter(User.class).save(user);//第二种通过ModelAdapter保存
                            //第三种是放在事务中
                            DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
                            definition.beginTransactionAsync(new ITransaction() {
                                @Override
                                public void execute(DatabaseWrapper databaseWrapper) {
                                    FlowManager.getModelAdapter(User.class).save(user);//第二种通过ModelAdapter保存
                                }
                            }).build().execute();*/
                Account.login(accountRspModel);//同步到xml持久化文件中
                if (accountRspModel.isBind()) {//如果是绑定状态，是否绑定设备
                    Account.setBind(true);//设置绑定状态
                    if (callback != null) {
                        callback.onDataLoaded(user);
                    }
                } else {//如果没有绑定好设备；要进行绑定设备
                    bindPush(callback);
                }
            } else {
                Factory.decodeRspCode(rspModel, callback);//错误解析
            }
        }

        /**
         * 网络请求失败
         *
         * @param call
         * @param t
         */
        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            if (callback != null) {
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        }
    }
}
