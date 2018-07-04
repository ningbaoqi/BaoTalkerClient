package project.com.ningbaoqi.baotalkerclient.activities;

import android.content.Context;
import android.content.Intent;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.fragment.account.UpdateInfoFragment;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.common.app.Fragment;

public class AccountActivity extends Activity {
    private Fragment mFragment;

    /**
     * 账户Activity显示的入口
     *
     * @param context 传递的上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mFragment = new UpdateInfoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container, mFragment).commit();
    }

    /**
     * Activity中收到图片剪切的回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFragment.onActivityResult(requestCode, resultCode, data);
    }
}
