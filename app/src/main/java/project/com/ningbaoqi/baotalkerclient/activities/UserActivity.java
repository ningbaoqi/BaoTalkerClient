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
import project.com.ningbaoqi.baotalkerclient.fragment.user.UpdateInfoFragment;
import project.com.ningbaoqi.common.app.Activity;
import project.com.ningbaoqi.common.app.Fragment;

/**
 * 用户信息界面可以提供用户信息修改
 */
public class UserActivity extends Activity {
    private Fragment mCurFragment;
    @BindView(R.id.im_bg)
    ImageView mBg;

    /**
     * 现实界面的入口方法
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }


    @Override
    protected void initWidget() {
        super.initWidget();
        mCurFragment = new UpdateInfoFragment();
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
