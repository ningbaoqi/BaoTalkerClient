package project.com.ningbaoqi.baotalkerclient.fragment.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.persistence.Account;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.MessageActivity;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.widget.a.PortraitView;
import project.com.ningbaoqi.common.widget.adapter.TextWatcherAdapter;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;

public abstract class ChatFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {
    protected String mReceiverId;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.edit_content)
    EditText mContent;
    @BindView(R.id.btn_submit)
    ImageView mSubmit;
    protected Adapter mAdapter;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initToolBar();
        initAppBar();
        initEditContent();
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new Adapter();
        mRecycler.setAdapter(mAdapter);
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
        // TODO
    }

    @OnClick(R.id.btn_record)
    void onRecordClick() {
        // TODO
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmit.isActivated()) {
            //发送消息
        } else {
            onMoreClick();
        }
    }

    /**
     * 打开更多
     */
    private void onMoreClick() {
        // TODO
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
            if (mLoading != null) {//必须是右边的才有可能需要重新发送
                // TODO 重新发送
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
