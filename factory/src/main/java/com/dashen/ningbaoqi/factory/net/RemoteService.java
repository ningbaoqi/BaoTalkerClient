package com.dashen.ningbaoqi.factory.net;

import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.account.AccountRspModel;
import com.dashen.ningbaoqi.factory.model.api.account.LoginModel;
import com.dashen.ningbaoqi.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 网络请求的所有的接口
 */
public interface RemoteService {
    /**
     * 网络请求一个注册接口
     *
     * @param registerModel 传入的RegisterModel
     * @return 返回的是 RspModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel registerModel);//设置为post请求并且registerModel作为body

    /**
     * 登陆接口
     *
     * @param loginModel
     * @return 用户信息
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel loginModel);//设置为post请求并且registerModel作为body

    /**
     * 绑定设备ID
     *
     * @param pushId 设备ID
     * @return 账户信息
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);//设置为post请求并且registerModel作为body
}
