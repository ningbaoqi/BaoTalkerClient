package project.com.ningbaoqi.baotalkerclient.fragment.message;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.persistence.Account;
import com.dashen.ningbaoqi.factory.presenter.message.ChatContract;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;
import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.MessageActivity;
import project.com.ningbaoqi.baotalkerclient.fragment.panel.PanelFragment;
import project.com.ningbaoqi.common.app.PresenterFragment;
import project.com.ningbaoqi.common.widget.a.PortraitView;
import project.com.ningbaoqi.common.widget.adapter.TextWatcherAdapter;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;

public abstract class ChatFragment<InitModel> extends PresenterFragment<ChatContract.Presenter> implements AppBarLayout.OnOffsetChangedListener, ChatContract.View<InitModel>, PanelFragment.PanelCallback {
    protected String mReceiverId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.edit_content)
    EditText mContent;
    @BindView(R.id.btn_submit)
    ImageView mSubmit;
    protected Adapter mAdapter;
    private AirPanel.Boss mPanelBoss;//控制底部面板与软件盘过渡的Boss控件
    private PanelFragment mPanelFragment;


    @Override
    protected final int getContentLayoutId() {
        return R.layout.fragment_chat_common;
    }

    /**
     * 得到顶部布局的ID资源
     *
     * @return
     */
    @LayoutRes
    protected abstract int getHeaderLayoutId();

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        ViewStub stub = root.findViewById(R.id.view_stub_header);//拿到占位布局;然后替换顶部布局，一定需要发生再Super之前，防止控件绑定异常
        stub.setLayoutResource(getHeaderLayoutId());
        stub.inflate();
        super.initWidget(root);
        //初始化面板操作
        mPanelBoss = root.findViewById(R.id.lay_content);
        mPanelBoss.setup(new AirPanel.PanelListener() {
            @Override
            public void requestHideSoftKeyboard() {//请求隐藏软件盘
                Util.hideKeyboard(mContent);
            }
        });
        mPanelFragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        mPanelFragment.setUp(this);
        initToolBar();
        initAppBar();
        initEditContent();
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    /**
     * 初始化ToolBar
     */
    protected void initToolBar() {
        Toolbar toolbar = mToolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * //给界面的AppBar设置一个监听，得到关闭与打开的时候的进度
     */
    private void initAppBar() {
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    private void initEditContent() {
        mContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                mSubmit.setActivated(needSendMsg);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {
        mPanelBoss.openPanel();//仅仅只需要打开即可
        mPanelFragment.showFace();
    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {
        mPanelBoss.openPanel();//仅仅只需要打开即可
        mPanelFragment.showRecord();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            //发送消息
            String content = mContent.getText().toString();
            mContent.setText("");
            mPresenter.pushText(content);
        } else {
            onMoreClick();
        }
    }

    /**
     * 打开更多
     */
    private void onMoreClick() {
        mPanelBoss.openPanel();//仅仅只需要打开即可
        mPanelFragment.showGallery();
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //界面没有占位布局，Recycler是一直显示的所以不需要做任何事情
    }

    @Override
    public EditText getInputEditText() {
        return mContent;//返回输入框
    }

    /**
     * 内容的适配器
     */
    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getUserId());
            switch (message.getType()) {
                case Message.TYPE_STR://文字类型
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;//本人发送的在右边，收到的在左边
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_left;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
                case R.layout.cell_chat_text_right://左右都是同一个
                case R.layout.cell_chat_text_left:
                    return new TextHolder(root);
                case R.layout.cell_chat_audio_right://左右都是同一个
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);
                case R.layout.cell_chat_pic_right://左右都是同一个
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);
                default:
                    return new TextHolder(root);//默认返回的就是Text类型进行处理
            }
        }
    }

    /**
     * Holder的基类
     */
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;
        @Nullable
        @BindView(R.id.loading)
        Loading mLoading;//左边没有右边有

        public BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            sender.load();//进行数据加载
            mPortrait.setup(Glide.with(ChatFragment.this), sender);//进行头像加载
            if (mLoading != null) {//当前的布局是在右边的
                int status = message.getStatus();
                if (status == Message.STATUS_DONE) {//如果状态是正常状态
                    mLoading.stop();
                    mLoading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {//如果是正在发送中的状态
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.setProgress(0);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    mLoading.start();
                } else if (status == Message.STATUS_FAILED) {//发送失败状态,允许重新发送
                    mLoading.setVisibility(View.VISIBLE);
                    mLoading.stop();
                    mLoading.setProgress(1);
                    mLoading.setForegroundColor(UiCompat.getColor(getResources(), R.color.alertImportant));
                }
                mPortrait.setEnabled(status == Message.STATUS_FAILED);//当状态是错误状态时才允许点击
            }
        }

        /**
         * 其实是我们的头像，来重新发送
         */
        @OnClick(R.id.im_portrait)
        void onRePushClick() {
            if (mLoading != null && mPresenter.rePush(mData)) {//必须是右边的才有可能需要重新发送;如果发送成功
                updateData(mData);//状态改变需要重新刷新界面当前的信息
            }
        }
    }

    /**
     * 文字的Holder
     */
    class TextHolder extends BaseHolder {
        @BindView(R.id.txt_content)
        TextView mContent;

        public TextHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            mContent.setText(message.getContent());//把内容设置到布局上
        }
    }

    /**
     * 语音的Holder
     */
    class AudioHolder extends BaseHolder {

        public AudioHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }

    /**
     * 图片的Holder
     */
    class PicHolder extends BaseHolder {
        public PicHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            // TODO
        }
    }
}
