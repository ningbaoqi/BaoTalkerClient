package project.com.ningbaoqi.baotalkerclient.fragment.search;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.SearchActivity.SearchFragment;
import project.com.ningbaoqi.common.app.Fragment;

/**
 * 搜索群的界面实现
 */
public class SearchGroupFragment extends Fragment implements SearchFragment {
    @Override
    public void search(String content) {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }
}
