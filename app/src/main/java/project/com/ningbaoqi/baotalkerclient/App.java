package project.com.ningbaoqi.baotalkerclient;

import com.dashen.ningbaoqi.factory.Factory;
import com.igexin.sdk.PushManager;

import project.com.ningbaoqi.common.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Factory.setUp();//调用Factory进行初始化
        PushManager.getInstance().initialize(this);//初始化推送
    }
}
