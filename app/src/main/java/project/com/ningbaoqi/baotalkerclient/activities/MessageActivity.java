package project.com.ningbaoqi.baotalkerclient.activities;

import android.content.Context;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.factory.model.Author;

public class MessageActivity extends Activity {

    /**
     * 显示一个人的聊天界面
     *
     * @param context
     * @param author
     */
    public static void show(Context context, Author author) {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}
