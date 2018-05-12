package project.com.ningbaoqi.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.ButterKnife;

/**
 * @author  ningbaoqi
 */
public abstract class Activity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();//在界面未初始化之前调用的初始化窗口

        if (initArgs(getIntent().getExtras())){
            int layoutId = getContentLayoutId();
            setContentView(layoutId);
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    //初始化窗口
    protected void initWindows(){

    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 如果参数正确返回true，错误返回false
     */
    protected boolean initArgs(Bundle bundle){
        return true;
    }

    protected abstract int getContentLayoutId();//得到当前界面的资源文件ID

    //初始化控件
    protected void initWidget(){
        ButterKnife.bind(this);
    }

    //初始化数据
    protected void initData(){

    }

    /**
     * 当点击界面导航返回时，finish当前界面
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 点击返回按键
     */
    @Override
    public void onBackPressed() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();//得到当前Activity下的所有Fragment
        if (fragments!=null && fragments.size() >0){
            for (Fragment fragment: fragments) {
                if (fragment instanceof project.com.ningbaoqi.common.app.Fragment){//如果Fragment是我们自己定义的Fragment
                    if (((project.com.ningbaoqi.common.app.Fragment) fragment).onBackPress()){
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
