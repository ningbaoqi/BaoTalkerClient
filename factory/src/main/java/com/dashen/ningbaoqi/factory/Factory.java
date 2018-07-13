package com.dashen.ningbaoqi.factory;

import android.util.Log;

import com.dashen.ningbaoqi.factory.data.group.GroupCenter;
import com.dashen.ningbaoqi.factory.data.group.GroupDispatcher;
import com.dashen.ningbaoqi.factory.data.message.MessageCenter;
import com.dashen.ningbaoqi.factory.data.message.MessageDispatcher;
import com.dashen.ningbaoqi.factory.data.user.UserCenter;
import com.dashen.ningbaoqi.factory.data.user.UserDispatcher;
import com.dashen.ningbaoqi.factory.model.api.PushModel;
import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.card.GroupCard;
import com.dashen.ningbaoqi.factory.model.card.GroupMemberCard;
import com.dashen.ningbaoqi.factory.model.card.MessageCard;
import com.dashen.ningbaoqi.factory.model.card.UserCard;
import com.dashen.ningbaoqi.factory.persistence.Account;
import com.dashen.ningbaoqi.factory.utils.DBFlowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.factory.data.DataSource;

public class Factory {
    private static final String TAG = Factory.class.getSimpleName();
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
                .setExclusionStrategies(new DBFlowExclusionStrategy())//设置一个过滤器，数据库级别的Model不进行Json转换
                .create();
    }

    /**
     * Factory中初始化
     */
    public static void setUp() {
        FlowManager.init(new FlowConfig.Builder(app()).openDatabasesOnInit(true).build());//数据库初始化的时候就开始打开数据库
        Account.load(app());//对持久化的数据进行初始化
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

    /**
     * 处理推送来的消息
     *
     * @param message
     */
    public static void dispatchPush(String message) {
        if (!Account.isLogin()) {//首先检查登陆状态
            return;
        }
        PushModel model = PushModel.decode(message);
        if (message == null) {
            return;
        }
        Log.d(TAG, model.toString());
        for (PushModel.Entity entity : model.getEntities()) {//对推送集合进行遍历;把数据放在数据中心处理
            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT://推出消息
                    instance.logout();
                    return;
                case PushModel.ENTITY_TYPE_MESSAGE://普通消息
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;
                case PushModel.ENTITY_TYPE_ADD_FRIEND://添加朋友
                    UserCard userCard = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(userCard);
                    break;
                case PushModel.ENTITY_TYPE_ADD_GROUP:
                    GroupCard groupCard = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(groupCard);
                    break;
                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS:
                    Type type = new TypeToken<List<GroupMemberCard>>() {
                    }.getType();
                    List<GroupMemberCard> cards = getGson().fromJson(entity.content, type);
                    getGroupCenter().dispatch(cards.toArray(new GroupMemberCard[0]));
                    break;
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS:
                    // TODO 成员退出的推送
                    break;
            }
        }
    }

    /**
     * 获取一个用户中心的实现类
     *
     * @return 用户中心的规范接口
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }

    /**
     * 获取一个消息中心的实现类
     *
     * @return 消息中心的规范接口
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }

    /**
     * 获取一个群处理中心的实现类
     *
     * @return 群中心的规范接口
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }
}
