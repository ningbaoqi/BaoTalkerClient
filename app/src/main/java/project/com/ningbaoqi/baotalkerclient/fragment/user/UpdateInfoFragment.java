package project.com.ningbaoqi.baotalkerclient.fragment.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dashen.ningbaoqi.factory.presenter.user.UpdateInfoPresenter;
import com.dashen.ningbaoqi.factory.presenter.user.UpdateInfoContract;
import com.yalantis.ucrop.UCrop;


import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.MainActivity;
import project.com.ningbaoqi.baotalkerclient.fragment.media.GalleryFragment;
import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.common.app.PresenterFragment;
import project.com.ningbaoqi.common.widget.a.PortraitView;

import static android.app.Activity.RESULT_OK;

/**
 * 用户更新信息的界面
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter> implements UpdateInfoContract.View {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    @BindView(R.id.im_sex)
    ImageView mSex;
    @BindView(R.id.edit_desc)
    EditText mDesc;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;
    private String mPortraitPath;//头像的本地路径
    private boolean isMan = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);//设置图片处理的格式JPEG
                options.setCompressionQuality(96);//设置图片压缩后的精度
                File file = Application.getPorttraitTmpFile();//本地头像缓存地址
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(file))
                        .withAspectRatio(1, 1)//保证正方形所以采用1：1
                        .withMaxResultSize(520, 520)//最大的大小时520*520像素
                        .withOptions(options).start(getActivity());//根据配置剪切
            }
        }).show(getChildFragmentManager(), GalleryFragment.class.getName());//show方法时建议使用getChildFragmentManager方法，因为这个是在Fragment当中，不然会用到Activity中的manager引起不必要的麻烦
    }

    /**
     * 收到从Activity中传递过来的回调，然后取出其中的数据，进行图片加载
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
        }
    }

    /**
     * 将截取的图片放在指定的控件中并且上传到OSS服务器上
     *
     * @param uri
     */
    private void loadPortrait(Uri uri) {
        mPortraitPath = uri.getPath();//得到头像地址
        Glide.with(this).load(uri).asBitmap().centerCrop().into(mPortrait);//将剪切得到的Uri设置到头像
    }

    @OnClick(R.id.im_sex)
    void onSexClick() {//性别图片点击的时候触发
        isMan = !isMan;
        Drawable drawable = getResources().getDrawable(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        mSex.setImageDrawable(drawable);
        mSex.getBackground().setLevel(isMan ? 0 : 1);//设置背景的层级切换颜色
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String desc = mDesc.getText().toString();
        mPresenter.update(mPortraitPath, desc, isMan);
    }

    /**
     * 注册错误的时候调用
     *
     * @param str
     */
    @Override
    public void showError(int str) {
        super.showError(str);
        //当提示需要显示错误的时候触发；一定是结束了
        mLoading.stop();//停止Loading
        mDesc.setEnabled(true);//让控件可以输入
        mPortrait.setEnabled(true);
        mSex.setEnabled(true);
        mSubmit.setEnabled(true);//提交按钮可以继续点击
    }

    /**
     * 正在加载的时候回调
     */
    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行时，界面不可操作
        mLoading.start();//开始Loading
        mDesc.setEnabled(false);
        mPortrait.setEnabled(false);
        mSex.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    /**
     * 初始化presenter
     *
     * @return
     */
    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }
}
