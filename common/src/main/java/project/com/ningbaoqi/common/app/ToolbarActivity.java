package project.com.ningbaoqi.common.app;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import project.com.ningbaoqi.common.R;

public abstract class ToolbarActivity extends Activity {
    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();
        initToolBar((Toolbar) findViewById(R.id.toolbar));
    }

    /**
     * 初始化toobar
     *
     * @param toolbar toolbar
     */
    public void initToolBar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }

    /**
     * 设置左上角的返回按钮是返回效果
     */
    protected void initTitleNeedBack() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
