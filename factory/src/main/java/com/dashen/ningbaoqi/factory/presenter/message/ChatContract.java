package com.dashen.ningbaoqi.factory.presenter.message;

import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.model.db.view.MemberUserModel;

import java.util.List;

import project.com.ningbaoqi.factory.presenter.BaseContract;

/**
 * 聊天契约
 */
public class ChatContract {

    public interface Presenter extends BaseContract.Presenter {
        void pushText(String content);//发送文字

        void pushAudio(String path);//发送语音

        void pushImages(String[] paths);//发送图片

        boolean rePush(Message message);// 重新发送一个消息，返回是否调度成功
    }

    public interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message> {
        void onInit(InitModel initModel);//初始化的Model
    }

    /**
     * 人聊天的界面
     */
    public interface UserView extends View<User> {

    }

    /**
     * 群聊天的界面
     */
    public interface GroupView extends View<Group> {
        void showAdminOption(boolean isAdmin);//显示是否是管理员

        void onInitGroupMembers(List<MemberUserModel> members, int moreCount);//初始化成员
    }
}
