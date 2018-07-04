package com.dashen.ningbaoqi.factory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import project.com.ningbaoqi.common.app.Application;

public class Factory {
    private static final Factory instance;
    private final Executor executor;

    static {
        instance = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);//新建一个4个线程的线程池
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
}
