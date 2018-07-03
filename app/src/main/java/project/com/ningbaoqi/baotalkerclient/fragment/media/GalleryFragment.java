package project.com.ningbaoqi.baotalkerclient.fragment.media;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import net.qiujuer.genius.ui.Ui;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.widget.a.GalleryView;

/**
 * 画廊图片选择Fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangedListener {//弹出框
    private GalleryView mGallery;
    private OnSelectedListener mListener;

    public GalleryFragment() {
    }

    /**
     * 设置事件监听并返回自己
     *
     * @param listener
     * @return
     */
    public GalleryFragment setListener(OnSelectedListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());//先使用一个默认的
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = root.findViewById(R.id.galleryView);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mGallery.setUp(getLoaderManager(), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        if (count > 0) {//如果选中了一张图片，隐藏自己
            dismiss();//
            if (mListener != null) {
                String[] path = mGallery.getSelectImagePath();//得到左右图片选择的路径
                mListener.onSelectedImage(path[0]);//返回第一张
                mListener = null;//取消和唤起者之间的引用，加快内存回收
            }
        }
    }

    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }

    private static class TranstatusButtomSheetDialog extends BottomSheetDialog {

        public TranstatusButtomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TranstatusButtomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TranstatusButtomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window = getWindow();
            if (window == null) {
                return;
            }
            int screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;//拿到屏幕的高度
            int statusBar = (int) Ui.dipToPx(getContext().getResources(), 25);//得到状态栏的高度
            int dialogHeight = screenHeight - statusBar;//对话框的高度
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        }
    }
}
