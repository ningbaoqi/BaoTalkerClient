package com.dashen.ningbaoqi.factory.data.message;

import android.support.annotation.NonNull;

import com.dashen.ningbaoqi.factory.data.BaseDbRepository;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.Message_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
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
        SQLite.select().from(Message.class).where(Message_Table.group_id.eq(receiverId))//无论是自己发还是别人发，只要是发到这个群的这个groupId就是ReceivedId
                .orderBy(Message_Table.createAt, false).limit(30).async().queryListResultCallback(this).execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        return message.getGroup() != null && receiverId.equalsIgnoreCase(message.getGroup().getId());//如果消息的group不为空，则一定是发送到一个群的，如果群Id等于我们需要的那就是通过了
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        Collections.reverse(tResult);//反转返回的集合，然后再进行调度
        super.onListQueryResult(transaction, tResult);
    }
}
