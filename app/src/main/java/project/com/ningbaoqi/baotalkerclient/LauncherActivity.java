package project.com.ningbaoqi.baotalkerclient;

import project.com.ningbaoqi.baotalkerclient.activities.MainActivity;
import project.com.ningbaoqi.baotalkerclient.fragment.assist.PermissionsFragment;
import project.com.ningbaoqi.common.app.Activity;

public class LauncherActivity extends Activity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }
}
