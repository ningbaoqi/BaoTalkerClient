package project.com.ningbaoqi.baotalkerclient.fragment.message;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.view.MemberUserModel;
import com.dashen.ningbaoqi.factory.presenter.message.ChatContract;
import com.dashen.ningbaoqi.factory.presenter.message.ChatGroupPresenter;

import java.util.List;

import butterknife.BindView;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.GroupMemberActivity;
import project.com.ningbaoqi.baotalkerclient.activities.PersonalActivity;

/**
 * 群聊天界面
 */
public class ChatGroupFragment extends ChatFragment<Group> implements ChatContract.GroupView {
    @BindView(R.id.im_header)
    ImageView mHeader;
    @BindView(R.id.lay_members)
    LinearLayout mLayMembers;
    @BindView(R.id.txt_member_more)
    TextView mMemberMore;

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        Glide.with(this).load(R.mipmap.default_banner_group).centerCrop().into(new ViewTarget<CollapsingToolbarLayout, GlideDrawable>(mCollapsingToolbarLayout) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setContentScrim(resource.getCurrent());
            }
        });
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
        View view = mLayMembers;
        if (view == null) {
            return;
        }
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

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }

    @Override
    public void onInit(Group group) {
        mCollapsingToolbarLayout.setTitle(group.getName());
        Glide.with(this).load(group.getPicture()).centerCrop().placeholder(R.mipmap.default_banner_group).into(mHeader);
    }

    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, long moreCount) {
        if (members == null || members.size() == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (final MemberUserModel member : members) {
            ImageView p = (ImageView) inflater.inflate(R.layout.lay_chat_group_potrait, mLayMembers, false);
            mLayMembers.addView(p, 0);//添加成员头像
            Glide.with(this).load(member.portrait).placeholder(R.drawable.default_portrait).centerCrop().dontAnimate().into(p);
            p.setOnClickListener(new View.OnClickListener() {//个人界面信息查看
                @Override
                public void onClick(View v) {
                    PersonalActivity.show(getContext(), member.userId);
                }
            });
        }
        if (moreCount > 0) {//更多的按钮
            mMemberMore.setText(String.format("+%s", moreCount));
            mMemberMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupMemberActivity.show(getContext(), mReceiverId);// 显示成员列表
                }
            });
        } else {
            mMemberMore.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAdminOption(boolean isAdmin) {
        if (isAdmin) {
            mToolbar.inflateMenu(R.menu.chat_group);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_add) {
                        GroupMemberActivity.showAdmin(getContext(), mReceiverId);//群成员添加 mReceiverId就是群的Id
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
