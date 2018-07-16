package com.dashen.ningbaoqi.factory.presenter.message;

import com.dashen.ningbaoqi.factory.data.helper.GroupHelper;
import com.dashen.ningbaoqi.factory.data.message.MessageGroupRepository;
import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.Message;

/**
 * 群聊天的逻辑
 */
public class ChatGroupPresenter extends ChatPresenter<ChatContract.GroupView> implements ChatContract.Presenter {
    public ChatGroupPresenter(ChatContract.GroupView view, String receiverId) {
        super(new MessageGroupRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();
        //拿到群的信息
        Group group = GroupHelper.findFromLocal(mReceiverId);
        if (group != null) {
            //初始化操作
        }
    }
}
