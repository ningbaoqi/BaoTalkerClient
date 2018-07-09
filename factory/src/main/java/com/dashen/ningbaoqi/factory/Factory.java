package com.dashen.ningbaoqi.factory;

import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.factory.data.DataSource;

public class Factory {
    private static final Factory instance;
    private final Executor executor;//全局的线程池
    private final Gson gson;//全局的Gson

    static {
        instance = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);//新建一个4个线程的线程池
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")//设置时间格式
//                // TODO 设置一个过滤器，数据库级别的Model不进行Json转换
//                .setExclusionStrategies()
                .create();
    }

    /**
     * 返回全局的Application
     *
     * @return
     */
    public static Application app() {
        return Application.getInstance();
    }


    /**
     * 异步运行的方法
     *
     * @param runnable
     */
    public static void runOnAsync(Runnable runnable) {
        instance.executor.execute(runnable);//拿到单例，拿到线程池，然后异步执行
    }

    /**
     * 返回一个全局的Gson；在这里可以进行Gson的一些全局的初始化
     *
     * @return Gson
     */
    public static Gson getGson() {
        return instance.gson;
    }


    /**
     * 进行错误code的解析；把网络返回的Code值进行统一的规划并返回一个String资源
     *
     * @param model    model
     * @param callback callback返回一个错误的资源ID
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null) {
            return;
        }
        //进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(final int resId, final DataSource.FailedCallback callback) {
        if (callback != null) {
            callback.onDataNotAvailable(resId);
        }
    }

    /**
     * 收到账户退出的消息需要进行账户退出重新登陆
     */
    private void logout() {

    }
}
