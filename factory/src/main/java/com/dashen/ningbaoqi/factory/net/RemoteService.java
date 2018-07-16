package com.dashen.ningbaoqi.factory.net;

import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.account.AccountRspModel;
import com.dashen.ningbaoqi.factory.model.api.account.LoginModel;
import com.dashen.ningbaoqi.factory.model.api.account.RegisterModel;
import com.dashen.ningbaoqi.factory.model.api.group.GroupCreateModel;
import com.dashen.ningbaoqi.factory.model.api.message.MsgCreateModel;
import com.dashen.ningbaoqi.factory.model.api.user.UserUpdateModel;
import com.dashen.ningbaoqi.factory.model.card.GroupCard;
import com.dashen.ningbaoqi.factory.model.card.MessageCard;
import com.dashen.ningbaoqi.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    /**
     * 用户更新的接口
     *
     * @param model 上传的诗句
     * @return 返回的数据
     */
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    /**
     * 搜索用户的操作
     *
     * @param name
     * @return
     */
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);

    /**
     * 用户关注接口
     *
     * @param userId
     * @return
     */
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String userId);


    /**
     * 获取联系人列表
     *
     * @return
     */
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    /**
     * 获取一个人信息的接口
     *
     * @return
     */
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    /**
     * 发送消息的接口
     *
     * @param model
     * @return
     */
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    /**
     * 创建群
     *
     * @param model
     * @return
     */
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    /**
     * 查找群
     *
     * @param groupId
     * @return
     */
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);
}
