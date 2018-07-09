package com.dashen.ningbaoqi.factory.net;

import com.dashen.ningbaoqi.factory.Factory;

import okhttp3.OkHttpClient;
import project.com.ningbaoqi.common.Common;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求的封装
 */
public class NetWork {
    /**
     * 构建一个Retrofit
     *
     * @return
     */
    public static Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().build();//得到一个OKhttp的Client

        Retrofit.Builder builder = new Retrofit.Builder();
        return builder.baseUrl(Common.Constance.API_URL)//设置电脑链接：服务器的地址
                .client(client)//设置JSON解析器
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
    }
}
