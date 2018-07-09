package project.com.ningbaoqi.baotalkerclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.data.helper.AccountHelper;
import com.dashen.ningbaoqi.factory.persistence.Account;
import com.igexin.sdk.PushConsts;

/**
 * 个推是通过广播接收器进行接受的
 */
public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = MessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        Bundle bundle = intent.getExtras();
        switch (bundle.getInt(PushConsts.CMD_ACTION)) {//判断当前消息的意图
            case PushConsts.GET_CLIENTID://当ID初始化的时候:
                Log.d(TAG, "GET_CLIENTID" + "-----" + bundle.toString());
                onClientInit(bundle.getString("clientId"));//获取设备ID
                break;
            case PushConsts.GET_MSG_DATA://常规的消息送达
                byte[] payLoad = bundle.getByteArray("payload");
                if (payLoad != null) {
                    String message = new String(payLoad);
                    Log.d(TAG, "GET_MSG_DATA" + "-----" + message);
                    onMessageArrived(message);
                }
                break;
            default:
                Log.d(TAG, "other" + "-----" + bundle.toString());
                break;
        }
    }

    /**
     * 当id初始化的时候
     *
     * @param cid 设备ID
     */
    private void onClientInit(String cid) {
        Account.setPushId(cid);//设置设备ID
        if (Account.isLogin()) {//如果目前是登录的状态就bindPush；没有登录的情况下是不能绑定PushID的
            AccountHelper.bindPush(null);
        }
    }

    /**
     * 消息到达时
     *
     * @param message 新消息
     */
    private void onMessageArrived(String message) {
        Factory.dispatchPush(message);//交给Factory处理
    }
}
