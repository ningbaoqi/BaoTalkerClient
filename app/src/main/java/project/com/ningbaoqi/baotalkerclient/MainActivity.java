package project.com.ningbaoqi.baotalkerclient;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.activities.AccountActivity;
import project.com.ningbaoqi.baotalkerclient.fragment.main.ActiveFragment;
import project.com.ningbaoqi.baotalkerclient.fragment.main.ContactFragment;
import project.com.ningbaoqi.baotalkerclient.fragment.main.GroupFragment;
import project.com.ningbaoqi.baotalkerclient.helper.NavHelper;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.common.widget.a.PortraitView;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {
    @BindView(R.id.appBar)
    View mAppBar;
    @BindView(R.id.im_potrait)
    PortraitView portraitView;
    @BindView(R.id.layout_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_action)
    FloatingActionButton mFloatingButton;
    private NavHelper mNavHelper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 使用Glide框架设置背景中间剪切
     */
    @Override
    protected void initWidget() {
        super.initWidget();
        mNavHelper = new NavHelper<>(this, R.id.layout_container, getSupportFragmentManager(), this);//初始化底部导航栏工具类
        mNavHelper.addTab(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .addTab(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .addTab(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        mNavigation.setOnNavigationItemSelectedListener(this);//添加底部导航栏监听
        Glide.with(this).load(R.mipmap.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mAppBar) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Menu menu = mNavigation.getMenu();//从底部导航中接管menu，然后手动的触发第一次点击
        menu.performIdentifierAction(R.id.action_home, 0);//触发menu事件
    }

    @OnClick(R.id.search)
    void onSearchMenuClick() {
    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
        AccountActivity.show(this);
    }

    /**
     * 当底部导航被点击的时候的回调方法
     *
     * @param item MenuItem
     * @return true 代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHelper.performClickMenu(item.getItemId());//转移事件流到工具类中
    }

    /**
     * 当界面处理完成回调
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        tvTitle.setText(newTab.extra);//从额外字段取出资源ID
        startAnimation(newTab);
    }

    /**
     * 启动浮动按钮动画
     *
     * @param newTab
     */
    private void startAnimation(NavHelper.Tab<Integer> newTab) {
        Log.d("nbq", "listener" + newTab);
        float rotation = 0f;
        float translationY = 0f;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            translationY = 250f;
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mFloatingButton.setImageResource(R.drawable.ic_group_add);
                rotation = -360f;
            } else {
                mFloatingButton.setImageResource(R.drawable.ic_contact_add);
                rotation = 360f;
            }
        }
        mFloatingButton.animate().rotation(rotation).translationY(translationY).setInterpolator(new AnticipateOvershootInterpolator(1)).setDuration(480).start();
    }
}
