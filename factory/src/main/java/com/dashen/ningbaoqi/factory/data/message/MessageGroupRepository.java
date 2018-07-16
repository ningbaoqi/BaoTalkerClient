package com.dashen.ningbaoqi.factory.data.message;

import android.support.annotation.NonNull;

import com.dashen.ningbaoqi.factory.data.BaseDbRepository;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * 群聊天的时候的聊天列表；关注的内容一定是我发给群的或者别人发送到群的信息
 */
public class MessageGroupRepository extends BaseDbRepository<Message> implements MessageDataSource {
    private String receiverId;//聊天的对象Id

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SucceedCallback<List<Message>> callback) {
        super.load(callback);

        // TODO
//        SQLite.select().from(Message.class).where(OperatorGroup.clause().and(Message_Table.sender_id.eq(receiverId)).and(Message_Table.group_id.isNull()))
//                .or(Message_Table.receiver_id.eq(receiverId)).orderBy(Message_Table.createAt, false).limit(30).async().queryListResultCallback(this).execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        return false;
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        Collections.reverse(tResult);//反转返回的集合，然后再进行调度
        super.onListQueryResult(transaction, tResult);
    }
}
