package com.dashen.ningbaoqi.factory.presenter.message;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.data.helper.MessageHelper;
import com.dashen.ningbaoqi.factory.data.message.MessageDataSource;
import com.dashen.ningbaoqi.factory.model.api.message.MsgCreateModel;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.persistence.Account;
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

    /**
     * 发送语音
     *
     * @param path
     */
    @Override
    public void pushAudio(String path, long time) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        MsgCreateModel model = new MsgCreateModel.Builder().receiver(mReceiverId, mReceiverType).content(path, Message.TYPE_AUDIO).attach(String.valueOf(time)).build();
        MessageHelper.push(model);
    }

    /**
     * 发送图片
     *
     * @param paths 此时路径是本地手机上的路径
     */
    @Override
    public void pushImages(String[] paths) {
        if (paths == null || paths.length == 0) {
            return;
        }
        for (String path : paths) {
            MsgCreateModel model = new MsgCreateModel.Builder().receiver(mReceiverId, mReceiverType).content(path, Message.TYPE_PIC).build();
            MessageHelper.push(model);
        }
    }

    @Override
    public boolean rePush(Message message) {
        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId()) && message.getStatus() == Message.STATUS_FAILED) {//允许重新发送的情况下
            message.setStatus(Message.STATUS_CREATED);//更改状态
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);//构建发送model
            MessageHelper.push(model);
            return true;
        }
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
