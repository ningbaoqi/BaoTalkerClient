package com.dashen.ningbaoqi.factory.persistence;

public class Account {
    private static String pushId = "test";//设备的推送ID

    public static String getPushId() {
        return pushId;
    }

    public static void setPushId(String pushId) {
        Account.pushId = pushId;
    }
}
