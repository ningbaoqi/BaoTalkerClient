package com.dashen.ningbaoqi.factory.data.user;

import com.dashen.ningbaoqi.factory.model.card.UserCard;

/**
 * 用户中心的基本定义
 */
public interface UserCenter {
    void dispatch(UserCard... userCards);//分发处理一堆卡片的信息，并更新到数据库
}
