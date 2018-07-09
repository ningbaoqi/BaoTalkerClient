package com.dashen.ningbaoqi.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.dashen.ningbaoqi.factory.Factory;

public class Account {
    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_IS_BIDN = "KEY_IS_BIDN";
    private static String pushId;//设备的推送ID
    private static boolean isBind;//设备ID是否已经绑定到了服务器

    /**
     * 存储数据到XML文件，持久化
     */
    private static void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(KEY_PUSH_ID, pushId).putBoolean(KEY_IS_BIDN, isBind).apply();//apply是一个异步操作，commit是同步的
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
        return true;
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
}
