package com.dashen.ningbaoqi.factory.data.group;

import com.dashen.ningbaoqi.factory.model.card.GroupCard;
import com.dashen.ningbaoqi.factory.model.card.GroupMemberCard;

/**
 * 群中心的接口定义
 */
public interface GroupCenter {

    void dispatch(GroupCard... cards);//群卡片的处理

    void dispatch(GroupMemberCard... cards);//群成员的处理
}
