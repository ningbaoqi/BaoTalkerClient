package project.com.ningbaoqi.baotalkerclient.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.model.db.Group;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.dashen.ningbaoqi.factory.model.db.Session;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.fragment.message.ChatGroupFragment;
import project.com.ningbaoqi.baotalkerclient.fragment.message.ChatUserFragment;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.factory.model.Author;

public class MessageActivity extends Activity {
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";//接收者ID，可以是群也可以是人的Id
    public static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";//标识是否是群
    private String mReceiverId;
    private boolean mIsGroup;

    /**
     * 显示一个人的聊天界面
     *
     * @param context
     * @param author
     */
    public static void show(Context context, Author author) {
        if (author == null || context == null || TextUtils.isEmpty(author.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, author.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(intent);
    }

    /**
     * 显示一个群的聊天界面
     *
     * @param context
     * @param group
     */
    public static void show(Context context, Group group) {
        if (group == null || context == null || TextUtils.isEmpty(group.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, group.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(intent);
    }

    /**
     * 通过Session
     *
     * @param context 上下文
     * @param session session
     */
    public static void show(Context context, Session session) {
        if (session == null || context == null || TextUtils.isEmpty(session.getId())) {
            return;
        }
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(KEY_RECEIVER_ID, session.getId());
        intent.putExtra(KEY_RECEIVER_IS_GROUP, session.getReceiverType() == Message.RECEIVER_TYPE_GROUP);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
        mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        return !TextUtils.isEmpty(mReceiverId);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        Fragment fragment;
        if (mIsGroup) {
            fragment = new ChatGroupFragment();
        } else {
            fragment = new ChatUserFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(KEY_RECEIVER_ID, mReceiverId);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.lay_container, fragment).commit();
    }
}
