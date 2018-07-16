package project.com.ningbaoqi.baotalkerclient.fragment.message;

import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.view.MemberUserModel;
import com.dashen.ningbaoqi.factory.presenter.message.ChatContract;
import com.dashen.ningbaoqi.factory.presenter.message.ChatGroupPresenter;

import java.util.List;

import project.com.ningbaoqi.baotalkerclient.R;

/**
 * 群聊天界面
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_group;
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(Group group) {

    }

    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, int moreCount) {

    }

    @Override
    public void showAdminOption(boolean isAdmin) {

    }
}
