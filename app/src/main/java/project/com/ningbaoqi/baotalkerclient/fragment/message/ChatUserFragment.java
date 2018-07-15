package project.com.ningbaoqi.baotalkerclient.fragment.message;

import android.support.design.widget.AppBarLayout;
import android.view.View;

import butterknife.BindView;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.widget.a.PortraitView;

/**
 * 用户聊天界面
 */
public class ChatUserFragment extends ChatFragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_user;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = mPortraitView;
        if (verticalOffset == 0) {//完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
        } else {
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {//关闭状态
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
            } else {//中间状态
                float process = 1 - verticalOffset / totalScrollRange;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(process);
                view.setScaleY(process);
                view.setAlpha(process);
            }
        }
    }
}
