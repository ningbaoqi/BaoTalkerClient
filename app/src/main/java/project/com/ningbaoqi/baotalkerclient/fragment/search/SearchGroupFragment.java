package project.com.ningbaoqi.baotalkerclient.fragment.search;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dashen.ningbaoqi.factory.model.card.GroupCard;
import com.dashen.ningbaoqi.factory.model.card.UserCard;
import com.dashen.ningbaoqi.factory.presenter.contact.FollowContract;
import com.dashen.ningbaoqi.factory.presenter.contact.FollowPresenter;
import com.dashen.ningbaoqi.factory.presenter.search.SearchContract;
import com.dashen.ningbaoqi.factory.presenter.search.SearchGroupPresenter;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.PersonalActivity;
import project.com.ningbaoqi.baotalkerclient.activities.SearchActivity;
import project.com.ningbaoqi.baotalkerclient.activities.SearchActivity.SearchFragment;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.app.PresenterFragment;
import project.com.ningbaoqi.common.widget.EmptyView;
import project.com.ningbaoqi.common.widget.a.PortraitView;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends PresenterFragment<SearchContract.Presenter> implements SearchActivity.SearchFragment, SearchContract.GroupView {
    private RecyclerAdapter<GroupCard> mAdapter;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.empty)
    EmptyView mEmptyView;

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupCard>() {
            @Override
            protected int getItemViewType(int position, GroupCard groupCard) {//返回cell的布局ID
                return R.layout.cell_search_group_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });
        //初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 发起首次搜索
        search("");
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        mAdapter.replace(groupCards);
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);//如果有数据则是OK，如果没有数据则显示空布局
    }

    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    /**
     * 每一个Cell 的布局操作
     */
    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {
        @BindView(R.id.portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.im_join)
        TextView mJoin;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCard groupCard) {
            mPortraitView.setup(Glide.with(SearchGroupFragment.this), groupCard.getPicture());
            mName.setText(groupCard.getName());
            mJoin.setEnabled(groupCard.getJoinAt() == null);//加入时间判断是否加入群
        }

        @OnClick(R.id.im_join)
        void onJoinClick() {
            PersonalActivity.show(getContext(), mData.getOwnerId());//进入创建者的个人界面
        }
    }
}
