package com.dashen.ningbaoqi.factory.presenter.message;

import com.dashen.ningbaoqi.factory.data.helper.UserHelper;
import com.dashen.ningbaoqi.factory.data.message.MessageRepository;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.User;

public class ChatUserPresenter extends ChatPresenter<ChatContract.UserView> implements ChatContract.Presenter {

    public ChatUserPresenter(ChatContract.UserView view, String receiverId) {
        super(new MessageRepository(receiverId), view, receiverId, Message.RECEIVER_TYPE_NONE);//数据源 View 接收者 接收者类型

    }

    @Override
    public void start() {
        super.start();
        User receiver = UserHelper.findFromLocal(mReceiverId);//从本地拿取这个人的信息
        getView().onInit(receiver);
    }
}
