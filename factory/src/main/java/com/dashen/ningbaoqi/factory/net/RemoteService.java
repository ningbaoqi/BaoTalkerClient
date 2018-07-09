package com.dashen.ningbaoqi.factory.net;

import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.account.AccountRspModel;
import com.dashen.ningbaoqi.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
    @POST
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel registerModel);//设置为post请求并且registerModel作为body

}
