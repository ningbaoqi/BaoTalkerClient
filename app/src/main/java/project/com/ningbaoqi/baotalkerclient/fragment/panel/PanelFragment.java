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

import net.qiujuer.genius.ui.Ui;

import java.util.List;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.tools.UiTool;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;
import project.com.ningbaoqi.face.Face;

/**
 * 面板Fragment
 */
public class PanelFragment extends Fragment {
    private PanelCallback mCallback;


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

    private void initFace(final View root) {
        final View facePanel = root.findViewById(R.id.lay_panel_face);
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

    private void initRecord(View root) {

    }

    private void initGallery(View root) {

    }

    public void showFace() {

    }

    public void showRecord() {

    }

    public void showGallery() {

    }

    /**
     * 回调聊天界面的Callback
     */
    public interface PanelCallback {
        EditText getInputEditText();
    }
}
