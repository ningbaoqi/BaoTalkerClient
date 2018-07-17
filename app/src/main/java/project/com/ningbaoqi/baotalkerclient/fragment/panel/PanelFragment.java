package project.com.ningbaoqi.baotalkerclient.fragment.panel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import net.qiujuer.genius.ui.Ui;

import java.io.File;
import java.util.List;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.tools.AudioRecordHelper;
import project.com.ningbaoqi.common.tools.UiTool;
import project.com.ningbaoqi.common.widget.AudioRecordView;
import project.com.ningbaoqi.common.widget.a.GalleryView;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;
import project.com.ningbaoqi.face.Face;

/**
 * 面板Fragment
 */
public class PanelFragment extends Fragment {
    private PanelCallback mCallback;
    private View mFacePanel, mGalleryPanel, mRecordPanel;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_panel;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initFace(root);
        initRecord(root);
        initGallery(root);
    }

    /**
     * 开始初始化方法
     *
     * @param callback
     */
    public void setUp(PanelCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 初始化表情
     *
     * @param root
     */
    private void initFace(final View root) {
        final View facePanel = mFacePanel = root.findViewById(R.id.lay_panel_face);
        View backSpace = facePanel.findViewById(R.id.im_backspace);
        backSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//删除逻辑
                PanelCallback callback = mCallback;
                if (callback == null) {
                    return;
                }
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);//模拟一个键盘点击
                callback.getInputEditText().dispatchKeyEvent(event);//模拟触发删除操作
            }
        });
        TabLayout tabLayout = facePanel.findViewById(R.id.tab);
        ViewPager viewPager = facePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);
        //每一个表情显示48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
        final int totalScreen = UiTool.getScreenWidth(getActivity());
        final int spanCount = totalScreen / minFaceSize;
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return Face.all(getContext()).size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            /**
             * 添加
             * @param container
             * @param position
             * @return
             */
            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
                List<Face.Bean> faces = Face.all(getContext()).get(position).faces;
                FaceAdapter adapter = new FaceAdapter(faces, new RecyclerAdapter.AdapterListenerImpl<Face.Bean>() {
                    @Override
                    public void onItemClick(RecyclerAdapter.ViewHolder holder, Face.Bean bean) {
                        if (mCallback == null) {
                            return;
                        }
                        EditText editText = mCallback.getInputEditText();//表情添加到输入框
                        Face.inputFace(getContext(), editText.getText(), bean, (int) (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));
                    }
                });
                recyclerView.setAdapter(adapter);
                container.addView(recyclerView);
                return recyclerView;
            }

            /**
             * 移除的
             * @param container
             * @param position
             * @param object
             */
            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            /**
             * 拿到表情盘的描述
             * @param position
             * @return
             */
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return Face.all(getContext()).get(position).name;
            }
        });
    }

    /**
     * 初始化语音
     *
     * @param root
     */
    private void initRecord(View root) {
        View recordView = mRecordPanel = root.findViewById(R.id.lay_panel_record);
        final AudioRecordView audioRecordView = recordView.findViewById(R.id.view_audio_record);
        File tmpFile = Application.getAudioTmpFile(true);//录音的缓存文件
        final AudioRecordHelper helper = new AudioRecordHelper(tmpFile, new AudioRecordHelper.RecordCallback() {//录音辅助工具类
            @Override
            public void onRecordStart() {

            }

            @Override
            public void onProgress(long time) {

            }

            @Override
            public void onRecordDone(File file, long time) {
                if (time < 1000) {//单位ms
                    return;
                }
                File audioFile = Application.getAudioTmpFile(false);
                if (file.renameTo(audioFile)) {//更改文件名
                    PanelCallback callback = mCallback;
                    if (callback != null) {//通知到聊天界面
                        callback.onRecordDone(audioFile, time);
                    }
                }
            }
        });
        audioRecordView.setup(new AudioRecordView.Callback() {//初始化
            @Override
            public void requestStartRecord() {//请求开始
                helper.recordAsync();
            }

            @Override
            public void requestStopRecord(int type) {//请求结束
                switch (type) {
                    case AudioRecordView.END_TYPE_CANCEL:
                    case AudioRecordView.END_TYPE_DELETE:
                        helper.stop(true);//删除和取消都代表想要取消
                        break;
                    case AudioRecordView.END_TYPE_NONE:
                    case AudioRecordView.END_TYPE_PLAY:
                        helper.stop(false);//想要发送
                        break;
                }
            }
        });
    }

    /**
     * 初始化画廊
     *
     * @param root
     */
    private void initGallery(View root) {
        final View galleryPanel = mGalleryPanel = root.findViewById(R.id.lay_gallery_panel);
        final GalleryView galleryView = galleryPanel.findViewById(R.id.view_gallery);
        final TextView selectedSize = galleryPanel.findViewById(R.id.txt_gallery_select_count);

        galleryView.setUp(getLoaderManager(), new GalleryView.SelectedChangedListener() {
            @Override
            public void onSelectedCountChanged(int count) {
                selectedSize.setText(String.format(getText(R.string.label_gallery_selected_size).toString(), count));
            }
        });
        galleryPanel.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGallerySendClick(galleryView, galleryView.getSelectImagePath());
            }
        });
    }

    /**
     * 点击的时候触发
     *
     * @param galleryView
     * @param paths
     */
    private void onGallerySendClick(GalleryView galleryView, String[] paths) {
        //通知给聊天界面
        galleryView.clear();
        PanelCallback callback = mCallback;
        if (callback == null) {
            return;
        }
        callback.onSendGallery(paths);
    }

    public void showFace() {
        mRecordPanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.VISIBLE);
    }

    public void showRecord() {
        mRecordPanel.setVisibility(View.VISIBLE);
        mGalleryPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.GONE);
    }

    public void showGallery() {
        mRecordPanel.setVisibility(View.GONE);
        mGalleryPanel.setVisibility(View.VISIBLE);
        mFacePanel.setVisibility(View.GONE);
    }

    /**
     * 回调聊天界面的Callback
     */
    public interface PanelCallback {
        EditText getInputEditText();

        void onSendGallery(String[] paths);//返回需要发送的图片

        void onRecordDone(File file, long time);//返回录音文件和时长
    }
}
