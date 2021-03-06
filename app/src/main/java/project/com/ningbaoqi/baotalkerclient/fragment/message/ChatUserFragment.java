package project.com.ningbaoqi.baotalkerclient.fragment.message;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.presenter.message.ChatContract;
import com.dashen.ningbaoqi.factory.presenter.message.ChatUserPresenter;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.PersonalActivity;
import project.com.ningbaoqi.common.widget.a.PortraitView;

/**
 * 用户聊天界面
 */
public class ChatUserFragment extends ChatFragment<User> implements ChatContract.UserView {
    @BindView(R.id.im_portrait)
    PortraitView mPortraitView;
    private MenuItem mUserInfoMenuItem;

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_user;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        Glide.with(this).load(R.mipmap.default_banner_chat).into(new ViewTarget<CollapsingToolbarLayout, GlideDrawable>(mCollapsingToolbarLayout) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setContentScrim(resource.getCurrent());
            }
        });
    }

    @Override
    protected void initToolBar() {
        super.initToolBar();
        Toolbar toolbar = mToolbar;
        toolbar.inflateMenu(R.menu.chat_user);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_person) {
                    onProtraitClick();
                }
                return false;
            }
        });
        mUserInfoMenuItem = toolbar.getMenu().findItem(R.id.action_person);//拿到菜单Icon
    }

    /**
     * 进行高度的综合运算，透明我们的头像和Icon
     *
     * @param appBarLayout
     * @param verticalOffset
     */
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = mPortraitView;
        MenuItem menuItem = mUserInfoMenuItem;
        if (view == null || menuItem == null) {
            return;
        }
        if (verticalOffset == 0) {//完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
            menuItem.setVisible(false);
            menuItem.getIcon().setAlpha(0);
        } else {
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {//关闭状态
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255);
            } else {//中间状态
                float process = 1 - verticalOffset / totalScrollRange;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(process);
                view.setScaleY(process);
                view.setAlpha(process);
                menuItem.setVisible(true);
                menuItem.getIcon().setAlpha(255 - (int) (255 * process));
            }
        }
    }

    @OnClick(R.id.im_portrait)
    void onProtraitClick() {
        PersonalActivity.show(getContext(), mReceiverId);
    }

    @Override
    public void onInit(User user) {
        mPortraitView.setup(Glide.with(this), user.getPortrait());
        mCollapsingToolbarLayout.setTitle(user.getName());
    }

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatUserPresenter(this, mReceiverId);//初始化Presenter
    }
}
