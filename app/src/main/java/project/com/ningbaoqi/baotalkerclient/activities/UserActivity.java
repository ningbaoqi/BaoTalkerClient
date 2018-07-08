package project.com.ningbaoqi.baotalkerclient.activities;

import android.content.Intent;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.fragment.user.UpdateInfoFragment;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.common.app.Fragment;

public class UserActivity extends Activity {
    private Fragment mCurFragment;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container, mCurFragment).commit();
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
        mCurFragment.onActivityResult(requestCode, resultCode, data);
    }
}
