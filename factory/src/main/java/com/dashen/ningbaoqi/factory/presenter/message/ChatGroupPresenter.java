package com.dashen.ningbaoqi.factory.presenter.message;

import com.dashen.ningbaoqi.factory.data.helper.GroupHelper;
import com.dashen.ningbaoqi.factory.data.message.MessageGroupRepository;
import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.view.MemberUserModel;
import com.dashen.ningbaoqi.factory.persistence.Account;

import java.util.List;

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
            ChatContract.GroupView view = getView();
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);
            view.onInit(group);//基础信息初始化
            List<MemberUserModel> models = group.getLatelyGroupMembers();//成员初始化
            final long memberCount = group.getGroupMemberCount();
            long moreCount = memberCount - models.size();//没有显示的成员的数量
            view.onInitGroupMembers(models, moreCount);
        }
    }
}

