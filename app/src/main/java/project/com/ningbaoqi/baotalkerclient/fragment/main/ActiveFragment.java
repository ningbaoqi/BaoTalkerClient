package project.com.ningbaoqi.baotalkerclient.fragment.main;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dashen.ningbaoqi.factory.model.db.Session;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.presenter.message.SessionContract;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.MessageActivity;
import project.com.ningbaoqi.baotalkerclient.activities.PersonalActivity;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.app.PresenterFragment;
import project.com.ningbaoqi.common.widget.EmptyView;
import project.com.ningbaoqi.common.widget.a.GalleryView;
import project.com.ningbaoqi.common.widget.a.PortraitView;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;
import project.com.ningbaoqi.utils.DateTimeUtil;


public class ActiveFragment extends PresenterFragment<SessionContract.Presenter> implements SessionContract.View {
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private RecyclerAdapter<Session> mAdapter;

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<Session>() {
            @Override
            protected int getItemViewType(int position, Session session) {//返回cell的布局ID
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ContactFragmentViewHolder(root);
            }
        });

        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Session session) {
                MessageActivity.show(getContext(), session);//点击到聊天界面
            }
        });
        //初始化占位布局
        mEmptyView.bind(mRecycler);
        setPlaceHolderView(mEmptyView);
    }

    /**
     * 第一次初始化的时候进行首次数据加载
     */
    @Override
    protected void initFirstData() {
        super.initFirstData();
        mPresenter.start();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return null;
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        mPlaceHolderView.triggerOkOrEmpty(mAdapter.getItemCount() > 0);
    }

    /**
     * 界面的数据渲染
     */
    class ContactFragmentViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.portrait)
        PortraitView mPortraitView;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.txt_content)
        TextView mContent;
        @BindView(R.id.txt_time)
        TextView mTime;


        public ContactFragmentViewHolder(View itemView) {
            super(itemView);
        }

        @OnClick(R.id.portrait)
        void onPortraitClick() {
            PersonalActivity.show(getContext(), mData.getId());
        }

        @Override
        protected void onBind(Session session) {
            mPortraitView.setup(Glide.with(ActiveFragment.this), session.getPicture());
            mName.setText(session.getTitle());
            mContent.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            mTime.setText(DateTimeUtil.getSimpleTime(session.getModifyAt()));
        }
    }
}
