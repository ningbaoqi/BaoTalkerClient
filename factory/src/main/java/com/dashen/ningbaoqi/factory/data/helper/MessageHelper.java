package com.dashen.ningbaoqi.factory.data.helper;

import android.os.SystemClock;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.model.api.RspModel;
import com.dashen.ningbaoqi.factory.model.api.message.MsgCreateModel;
import com.dashen.ningbaoqi.factory.model.card.MessageCard;
import com.dashen.ningbaoqi.factory.model.card.UserCard;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.Message_Table;
import com.dashen.ningbaoqi.factory.net.NetWork;
import com.dashen.ningbaoqi.factory.net.RemoteService;
import com.dashen.ningbaoqi.factory.net.UploadHelper;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.File;
import java.util.concurrent.ExecutionException;

import project.com.ningbaoqi.common.Common;
import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.utils.PicturesCompressor;
import project.com.ningbaoqi.utils.StreamUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 消息工具类
 */
public class MessageHelper {

    /**
     * 从本地进行查找
     *
     * @param id
     * @return
     */
    public static Message findFromLocal(String id) {
        return SQLite.select().from(Message.class).where(Message_Table.id.eq(id)).querySingle();
    }

    /**
     * 进行网络发送；异步进行
     *
     * @param model
     */
    public static void push(final MsgCreateModel model) {
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {//如果是一个已经发送过的消息则不能重新发送，如果是文件类型的(语音、图片、文件)需要先上传后才发送；如果是文本消息，则直接发送;再发送的时候需要进行界面更新状态Card
                Message message = findFromLocal(model.getId());
                if (message != null && message.getStatus() != Message.STATUS_FAILED) {//如果是已经发送的消息不能重新发送
                    return;
                }
                final MessageCard card = model.buildCard();//再发送的时候需要通知界面更新状态：card
                Factory.getMessageCenter().dispatch(card);

                //发送文件消息分两步：上传到云服务器，消息push到我们自己的服务器

                if (card.getType() != Message.TYPE_STR) {//不是文字类型的
                    if (!card.getContent().startsWith(UploadHelper.ENDPOINT)) {//没有上传到云服务器；还是本地手机文件
                        String content;
                        switch (card.getType()) {
                            case Message.TYPE_PIC:
                                content = uploadPicture(card.getContent());
                                break;
                            case Message.TYPE_AUDIO:
                                content = uploadAudio(card.getContent());
                                break;
                            default:
                                content = "";
                                break;
                        }
                        if (TextUtils.isEmpty(content)) {
                            card.setStatus(Message.STATUS_FAILED);//失败
                            Factory.getMessageCenter().dispatch(card);
                            return;
                        }
                        card.setContent(content);//成功则把网络路径进行替换
                        Factory.getMessageCenter().dispatch(card);
                        model.refreshByCard();//因为卡片的内容改变了；而我们上传到服务器使用的是model，所以model也需要跟着更改
                    }
                }

                RemoteService service = NetWork.remote();//直接发送，进行网络调度
                service.msgPush(model).enqueue(new Callback<RspModel<MessageCard>>() {

                    @Override
                    public void onResponse(Call<RspModel<MessageCard>> call, Response<RspModel<MessageCard>> response) {
                        RspModel<MessageCard> rspModel = response.body();
                        if (rspModel != null && rspModel.success()) {
                            MessageCard rspCard = rspModel.getResult();
                            if (rspCard != null) {
                                Factory.getMessageCenter().dispatch(rspCard);//成功的调度
                            }
                        } else {
                            Factory.decodeRspCode(rspModel, null);//解析是否是账户异常
                            onFailure(call, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<RspModel<MessageCard>> call, Throwable t) {//通知失败
                        card.setStatus(Message.STATUS_FAILED);
                        Factory.getMessageCenter().dispatch(card);
                    }
                });
            }
        });
    }

    /**
     * 上传图片
     *
     * @param path
     * @return
     */
    private static String uploadPicture(String path) {
        File file = null;
        try {
            file = Glide.with(Factory.app()).load(path).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();//通过Glide的缓存区间解决了图片外部权限的问题
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file != null) {
            //进行压缩
            String cacheDir = Application.getCacheDirFile().getAbsolutePath();
            String tempFile = String.format("%s/image/Cache_%s.png", cacheDir, SystemClock.uptimeMillis());
            try {
                if (PicturesCompressor.compressImage(file.getAbsolutePath(), tempFile, Common.Constance.MAX_UPLOAD_IMAGE_LENGTH)) {
                    String ossPath = UploadHelper.uploadImage(tempFile);//上传
                    StreamUtil.delete(tempFile);//清理缓存
                    return ossPath;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 上传语音
     *
     * @param content
     * @return
     */
    private static String uploadAudio(String content) {
        File file = new File(content);
        if (!file.exists() || file.length() <= 0) {
            return null;
        }
        return UploadHelper.uploadAudio(content);
    }

    /**
     * 查询一个消息，这个消息是一个群中的最后一条消息
     *
     * @param groupId 群ID
     * @return 群中聊天的最后一条消息
     */
    public static Message findLastWithGroup(String groupId) {
        return SQLite.select().from(Message.class).where(Message_Table.group_id.eq(groupId)).orderBy(Message_Table.createAt, false).querySingle();
    }

    /**
     * 查询一个消息；这个消息是和一个人的最后一条聊天消息
     *
     * @param userId
     * @return
     */
    public static Message findLastWIthUser(String userId) {
        return SQLite.select().from(Message.class).where(OperatorGroup.clause().and(Message_Table.sender_id.eq(userId)).and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(userId)).orderBy(Message_Table.createAt, false).querySingle();
    }
}