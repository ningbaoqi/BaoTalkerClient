package project.com.ningbaoqi.baotalkerclient.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.model.db.Group;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.factory.model.Author;

public class MessageActivity extends Activity {
    private static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";//接收者ID，可以是群也可以是人的Id
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";//标识是否是群
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

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }

    
}
