package project.com.ningbaoqi.baotalkerclient;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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

import net.qiujuer.genius.ui.Ui;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.fragment.main.ActiveFragment;
import project.com.ningbaoqi.baotalkerclient.fragment.main.ContactFragment;
import project.com.ningbaoqi.baotalkerclient.fragment.main.GroupFragment;
import project.com.ningbaoqi.baotalkerclient.helper.NavHelper;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.common.widget.a.PortraitView;

public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {
    @BindView(R.id.appbar)
    View mLayAppBar;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    @BindView(R.id.btn_action)
    FloatingActionButton mAction;
    private NavHelper<Integer> mNavHalper;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mNavHalper = new NavHelper<>(this, R.id.lay_container, getSupportFragmentManager(), this);//初始化工具类
        mNavHalper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this).load(R.mipmap.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppBar) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        Menu menu = mNavigation.getMenu();//从底部导航中接管我们的Menu，然后进行手动的触发第一次点击
        menu.performIdentifierAction(R.id.action_home, 0);//触发首次选中home
    }

    @OnClick(R.id.im_search)
    public void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    public void onActionClick() {

    }

    /**
     * 当底部导航栏被点击的时候触发
     *
     * @param item MenuItem
     * @return true 代表我们能够处理这个点击
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHalper.performClickMenu(item.getItemId());//转接事件流到工具类中
    }

    /**
     * NavHelper 处理后回调的方法
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        mTitle.setText(newTab.extra);//从额外字段中取出我们的title资源ID
        /**
         * 对浮动按钮进行隐藏与显示的动画
         */
        float translationY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            translationY = Ui.dipToPx(getResources() , 76);//主界面隐藏浮动按钮
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                mAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
        mAction.animate().rotation(rotation).translationY(translationY).setDuration(480).setInterpolator(new AnticipateOvershootInterpolator()).start();//设置弹性效果插值器
    }
}
