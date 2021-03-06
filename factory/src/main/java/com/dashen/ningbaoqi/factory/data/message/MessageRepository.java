package com.dashen.ningbaoqi.factory.data.message;

import android.support.annotation.NonNull;

import com.dashen.ningbaoqi.factory.data.BaseDbRepository;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 跟某人聊天的时候的聊天列表；关注的内容一定是我发给这个人的，或者是它发送给我的
 */
public class MessageRepository extends BaseDbRepository<Message> implements MessageDataSource {
    private String receiverId;//聊天的对象Id

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);
        SQLite.select().from(Message.class).where(OperatorGroup.clause().and(Message_Table.sender_id.eq(receiverId)).and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId)).orderBy(Message_Table.createAt, false).limit(30).async().queryListResultCallback(this).execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        return (receiverId.equalsIgnoreCase(message.getSender().getId()) && message.getGroup() == null)//确定一定是发送给一个人的
                || (message.getReceiver() != null && receiverId.equalsIgnoreCase(message.getReceiver().getId()));//如果消息的接收者不为空，那么一定是要发送给某个人的，这个人只要是我或者某个人；如果这个“某个人”就是receiverId,那么就是我要关注的信息
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        Collections.reverse(tResult);//反转返回的集合，然后再进行调度
        super.onListQueryResult(transaction, tResult);
    }
}
