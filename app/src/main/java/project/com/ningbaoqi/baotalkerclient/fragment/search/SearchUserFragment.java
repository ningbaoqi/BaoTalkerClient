package project.com.ningbaoqi.baotalkerclient.fragment.search;

import android.support.v7.widget.RecyclerView;

import com.dashen.ningbaoqi.factory.model.card.UserCard;
import com.dashen.ningbaoqi.factory.presenter.search.SearchContract;
import com.dashen.ningbaoqi.factory.presenter.search.SearchUserPresenter;

import java.util.List;

import butterknife.BindView;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.SearchActivity;
import project.com.ningbaoqi.common.app.PresenterFragment;
import project.com.ningbaoqi.common.widget.EmptyView;

/**
 * 搜索人的界面实现
 */
public class SearchUserFragment extends PresenterFragment<SearchContract.Presenter> implements SearchActivity.SearchFragment, SearchContract.UserView {
    @BindView(R.id.empty)
    EmptyView mEmptyView;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void search(String content) {
        mPresenter.search(content);
    }

    /**
     * 数据成功的情况下返回数据
     *
     * @param userCards 返回的数据
     */
    @Override
    public void onSearchDone(List<UserCard> userCards) {

    }

    /**
     * 初始化Presenter
     *
     * @return 返回Presenter
     */
    @Override
    protected SearchContract.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }
}
