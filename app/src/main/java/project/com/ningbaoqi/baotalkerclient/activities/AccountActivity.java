package project.com.ningbaoqi.baotalkerclient.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.fragment.account.AccountTrigger;
import project.com.ningbaoqi.baotalkerclient.fragment.account.LoginFragment;
import project.com.ningbaoqi.baotalkerclient.fragment.account.RegisterFragment;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.common.app.Fragment;

public class AccountActivity extends Activity implements AccountTrigger {
    private Fragment mCurFragment;
    private Fragment mLoginFragment;
    private Fragment mRegisterFragment;
    @BindView(R.id.im_bg)
    ImageView mBg;

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
        //初始化Fragment
        mCurFragment = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_container, mCurFragment).commit();

        //初始化背景
        Glide.with(this).load(R.drawable.bg_src_tianjin).centerCrop().into(new ViewTarget<ImageView, GlideDrawable>(mBg) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                Drawable drawable = resource.getCurrent();//拿到glide的drawable
                drawable = DrawableCompat.wrap(drawable);//使用适配类进行包装
                drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent), PorterDuff.Mode.SCREEN);//设置着色效果和颜色，蒙版模式
                this.view.setImageDrawable(drawable);//设置给ImageView
            }
        });
    }

    /**
     * 切换Fragment
     */
    @Override
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == mLoginFragment) {
            if (mRegisterFragment == null) {
                mRegisterFragment = new RegisterFragment();//默认情况下为null，第一次之后就不为null了
            }
            fragment = mRegisterFragment;
        } else {
            fragment = mLoginFragment;//因为默认情况下已经赋值，无需判断null
        }
        mCurFragment = fragment;//重新赋值当前正在显示的Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_container, fragment).commit();//切换显示
    }
}
