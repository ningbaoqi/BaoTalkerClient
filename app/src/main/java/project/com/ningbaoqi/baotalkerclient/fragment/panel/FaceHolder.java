package project.com.ningbaoqi.baotalkerclient.fragment.panel;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;

import butterknife.BindView;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;
import project.com.ningbaoqi.face.Face;

public class FaceHolder extends RecyclerAdapter.ViewHolder<Face.Bean> {
    @BindView(R.id.im_face)
    ImageView mFace;

    public FaceHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void onBind(Face.Bean bean) {
        if (bean != null && ((bean.preview instanceof Integer) || bean.preview instanceof String)) {//一种是drawable，一种是zip文件形式
            Glide.with(mFace.getContext()).load(bean.preview).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(mFace);//设置解码的格式，为了保证清晰度
        }
    }
}
