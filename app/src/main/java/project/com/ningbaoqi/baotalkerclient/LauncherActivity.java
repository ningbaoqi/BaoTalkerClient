package project.com.ningbaoqi.baotalkerclient;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.support.graphics.drawable.ArgbEvaluator;
import android.text.TextUtils;
import android.util.Log;
import android.util.Property;
import android.view.View;

import com.dashen.ningbaoqi.factory.persistence.Account;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

import project.com.ningbaoqi.baotalkerclient.activities.AccountActivity;
import project.com.ningbaoqi.baotalkerclient.activities.MainActivity;
import project.com.ningbaoqi.baotalkerclient.fragment.assist.PermissionsFragment;
import project.com.ningbaoqi.common.app.Activity;

public class LauncherActivity extends Activity {

    //Drawable
    private ColorDrawable mBgDrawable;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        View root = findViewById(R.id.activity_launch);//拿到根布局
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);//获取颜色
        ColorDrawable drawable = new ColorDrawable(color);//创建一个drawable
        root.setBackground(drawable);//设置给背景
        mBgDrawable = drawable;
    }

    @Override
    protected void initData() {
        super.initData();
        startAnim(0.5f, new Runnable() {//动画进入到50%等待pushID获取到
            @Override
            public void run() {
                waitPushReceiverId();//检查等待状态
            }
        });
    }

    /**
     * 等待个推框架对我们的PushId设置好值
     */
    private void waitPushReceiverId() {
        if (Account.isLogin()) {//如果已经进行了登录
            if (Account.isBind()) {//已经登录判断是否已经绑定到服务器；如果没有绑定则等待广播接收器进行绑定
                skip();
                return;
            }
        } else {//没有登录,没有登录的情况下是不能绑定PushID的
            if (!TextUtils.isEmpty(Account.getPushId())) {//如果拿到了:就进行跳转
                skip();
                return;
            }
        }
        getWindow().getDecorView().postDelayed(new Runnable() {// 循环等待
            @Override
            public void run() {
                waitPushReceiverId();
            }
        }, 500);
    }

    /**
     * 在跳转之前需要把剩下的50%进行完成
     */
    private void skip() {
        startAnim(1f, new Runnable() {
            @Override
            public void run() {
                reallySkip();
            }
        });
    }

    /**
     * 真正的跳转
     */
    private void reallySkip() {
        if (PermissionsFragment.haveAll(LauncherActivity.this, getSupportFragmentManager())) {
            //检查跳转到主页还是登录页面
            if (Account.isLogin()) {
                MainActivity.show(LauncherActivity.this);
            } else {
                AccountActivity.show(LauncherActivity.this);
            }
            finish();
        }
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProcess  动画的结束进度
     * @param endCallback 动画结束时触发
     */
    @SuppressLint("RestrictedApi")
    private void startAnim(float endProcess, final Runnable endCallback) {
        int finalColor = Resource.Color.WHITE;//获取一个结束时的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProcess, mBgDrawable.getColor(), finalColor);//运算当前进度的颜色
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, evaluator, endColor);//构建一个属性动画
        valueAnimator.setDuration(1500);
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);//设置开始结束值
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();//结束时触发
            }
        });
        valueAnimator.start();
    }


    private final Property<LauncherActivity, Object> property = new Property<LauncherActivity, Object>(Object.class, "color") {
        @Override
        public Object get(LauncherActivity object) {
            return mBgDrawable.getColor();
        }

        @Override
        public void set(LauncherActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }
    };
}
