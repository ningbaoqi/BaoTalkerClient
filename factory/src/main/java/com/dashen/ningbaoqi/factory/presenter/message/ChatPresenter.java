package com.dashen.ningbaoqi.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.dashen.ningbaoqi.factory.data.helper.MessageHelper;
import com.dashen.ningbaoqi.factory.data.message.MessageDataSource;
import com.dashen.ningbaoqi.factory.model.api.message.MsgCreateModel;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.presenter.BaseSourcePresenter;
import com.dashen.ningbaoqi.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 * 聊天Presenter的基础类
 */
public class ChatPresenter<View extends ChatContract.View> extends BaseSourcePresenter<Message, Message, MessageDataSource, View> implements ChatContract.Presenter {
    protected String mReceiverId;//接收者Id可能是群也可能是人
    protected int mReceiverType;//区分是人还是群的ID

    public ChatPresenter(MessageDataSource source, View view, String receiverId, int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        MsgCreateModel model = new MsgCreateModel.Builder().receiver(mReceiverId, mReceiverType).content(content, Message.TYPE_STR).build();
        MessageHelper.push(model);//进行网络发送
    }

    @Override
    public void pushAudio(String path) {
        // TODO 发送语音
    }

    @Override
    public void pushImages(String[] paths) {
        // TODO 发送图片
    }

    @Override
    public boolean rePush(Message message) {
        return false;
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View view = getView();
        if (view == null) {
            return;
        }
        List<Message> old = view.getRecyclerAdapter().getItems();//拿到老数据
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);//差异计算
        refreshData(result, messages);//进行界面刷新
    }
}
