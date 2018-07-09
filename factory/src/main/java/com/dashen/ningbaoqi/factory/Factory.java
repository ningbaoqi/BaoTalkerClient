package com.dashen.ningbaoqi.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import project.com.ningbaoqi.common.app.Application;

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
}
