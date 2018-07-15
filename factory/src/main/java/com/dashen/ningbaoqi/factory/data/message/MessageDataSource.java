package com.dashen.ningbaoqi.factory.data.message;

import com.dashen.ningbaoqi.factory.model.db.Message;

import project.com.ningbaoqi.factory.data.DbDataSource;

/**
 * 消息的数据源定义，它的实现是MessageRepostory;关注的是Message表
 */
public interface MessageDataSource extends DbDataSource<Message> {
}
