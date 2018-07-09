package com.dashen.ningbaoqi.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.model.api.account.AccountRspModel;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.model.db.User_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

public class Account {
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIDN = "KEY_IS_BIDN";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    private static String pushId;//设备的推送ID
    private static boolean isBind;//设备ID是否已经绑定到了服务器
    private static String token;//登录状态的token，用来接口请求
    private static String userId;//登录的用户ID
    private static String account;//登录的账户

    /**
     * 存储数据到XML文件，持久化
     */
    private static void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_IS_BIDN, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();//apply是一个异步操作，commit是同步的
    }

    /**
     * 进行数据加载
     *
     * @param context 上下文
     */
    public static void load(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        pushId = sharedPreferences.getString(KEY_PUSH_ID, "");
        isBind = sharedPreferences.getBoolean(KEY_IS_BIDN, false);
        token = sharedPreferences.getString(KEY_TOKEN, "");
        userId = sharedPreferences.getString(KEY_USER_ID, "");
        account = sharedPreferences.getString(KEY_ACCOUNT, "");
    }


    /**
     * 获取推送ID
     *
     * @return
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 设置并存储设备的ID
     *
     * @param pushId 设备的推送ID
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        Account.save(Factory.app());
    }


    /**
     * 返回当前帐号是否登录
     *
     * @return 是否登录的状态
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);//用户ID和token不为空
    }

    /**
     * 是否已经完善了用户信息
     *
     * @return true 完成了
     */
    public static boolean isComplete() {
        // TODO
        return isLogin();
    }

    /**
     * 是否已经绑定到了服务器
     *
     * @return 返回绑定状态
     */
    public static boolean isBind() {
        return isBind;
    }


    /**
     * 设置绑定状态
     *
     * @param isBind
     */
    public static void setBind(boolean isBind) {
        Account.isBind = isBind;
        Account.save(Factory.app());
    }

    /**
     * 保存我自己的信息到持久化XML中
     *
     * @param model model
     */
    public static void login(AccountRspModel model) {
        //存储当前登录的账户 token，用户ID，当便从数据库中查询我的信息
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        Account.save(Factory.app());
    }

    /**
     * 从数据库当中去查询
     *
     * @return
     */
    public static User getUser() {
        return !TextUtils.isEmpty(userId) ? new User() : SQLite.select().from(User.class).where(User_Table.id.eq(userId)).querySingle();//在数据库中查询
    }

    /**
     * 获取当前登录的token
     *
     * @return token
     */
    public static String getToken() {
        return token;
    }
}
