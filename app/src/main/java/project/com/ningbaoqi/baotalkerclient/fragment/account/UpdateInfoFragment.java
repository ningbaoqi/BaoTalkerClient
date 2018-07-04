package project.com.ningbaoqi.baotalkerclient.fragment.account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.net.UploadHelper;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.fragment.media.GalleryFragment;
import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.widget.a.PortraitView;

import static android.app.Activity.RESULT_OK;

/**
 * 用户更新信息的界面
 */
public class UpdateInfoFragment extends Fragment {
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;

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
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 将截取的图片放在指定的控件中并且上传到OSS服务器上
     *
     * @param uri
     */
    private void loadPortrait(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(mPortrait);//将剪切得到的Uri设置到头像
        final String localPath = uri.getPath();//获取本地文件的地址
        Log.d("nbq", "localPath=========" + localPath);
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = UploadHelper.uploadPortrait(localPath);
                Log.d("nbq", "result url ========" + url);
            }
        });
    }
}
