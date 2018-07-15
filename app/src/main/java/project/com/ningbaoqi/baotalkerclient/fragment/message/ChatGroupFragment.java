package project.com.ningbaoqi.baotalkerclient.fragment.message;

import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.presenter.message.ChatContract;

import project.com.ningbaoqi.baotalkerclient.R;

/**
 * 群聊天界面
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {
    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    public void onInit(Group group) {

    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return null;
    }
}
