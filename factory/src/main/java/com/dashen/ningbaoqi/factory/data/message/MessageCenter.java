package com.dashen.ningbaoqi.factory.data.message;

import com.dashen.ningbaoqi.factory.model.card.MessageCard;

/**
 * 消息中心：进行消息卡片的消费
 */
public interface MessageCenter {
    void dispatch(MessageCard... cards);
}
