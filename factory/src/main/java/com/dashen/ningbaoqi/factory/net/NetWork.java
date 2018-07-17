package com.dashen.ningbaoqi.factory.net;

import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.persistence.Account;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import project.com.ningbaoqi.common.Common;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 */
public class NetWork {
    private static NetWork instance;
    private Retrofit retrofit;
    private OkHttpClient client;

    static {
        instance = new NetWork();
    }

    private NetWork() {
    }

    public static OkHttpClient getClient() {
        if (instance.client != null) {
            return instance.client;
        }
        instance.client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {//添加一个给所有的请求添加的拦截器
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();//拿到我们的请求
                Request.Builder builder = original.newBuilder();//重新进行Builder
                if (!TextUtils.isEmpty(Account.getToken())) {
                    builder.addHeader("token", Account.getToken());//注入一个token
                }
                builder.addHeader("Content-Type", "application/json");
                Request newRequest = builder.build();
                return chain.proceed(newRequest);//返回
            }
        }).build();//得到一个OKhttp的Client
        return instance.client;
    }

    /**
     * 构建一个Retrofit
     *
     * @return
     */
    public static Retrofit getRetrofit() {
        if (instance.retrofit != null) {
            return instance.retrofit;
        }
        OkHttpClient client = getClient();
        Retrofit.Builder builder = new Retrofit.Builder();
        instance.retrofit = builder.baseUrl(Common.Constance.API_URL)//设置电脑链接：服务器的地址
                .client(client)//设置JSON解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
        return instance.retrofit;
    }

    /**
     * 返回一个请求代理
     *
     * @return
     */
    public static RemoteService remote() {
        return NetWork.getRetrofit().create(RemoteService.class);
    }
}
