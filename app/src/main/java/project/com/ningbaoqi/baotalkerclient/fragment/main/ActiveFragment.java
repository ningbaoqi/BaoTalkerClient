package project.com.ningbaoqi.baotalkerclient.fragment.main;

import butterknife.BindView;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.widget.a.GalleryView;


public class ActiveFragment extends Fragment {
    @BindView(R.id.gallery)
    GalleryView mGallery;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();
        mGallery.setUp(getLoaderManager(), new GalleryView.SelectedChangedListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
