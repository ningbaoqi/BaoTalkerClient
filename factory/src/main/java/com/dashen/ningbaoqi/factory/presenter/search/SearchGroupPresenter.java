package com.dashen.ningbaoqi.factory.presenter.search;

import com.dashen.ningbaoqi.factory.data.helper.GroupHelper;
import com.dashen.ningbaoqi.factory.model.card.GroupCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.factory.presenter.BasePresenter;
import retrofit2.Call;

/**
 * 搜索群的实现
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter, DataSource.Callback<List<GroupCard>> {
    private Call searchCall;

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        if (call != null && !call.isCanceled()) {//如果有上一次的请求并且没有取消则调用取消操作
            call.cancel();
        }
        searchCall = GroupHelper.search(content, this);
    }

    /**
     * 搜索成功
     *
     * @param groupCards
     */
    @Override
    public void onDataLoaded(final List<GroupCard> groupCards) {
        final SearchContract.GroupView groupView = getView();
        if (groupView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    groupView.onSearchDone(groupCards);
                }
            });
        }
    }

    /**
     * 搜索失败
     *
     * @param strRes
     */
    @Override
    public void onDataNotAvailable(final int strRes) {
        final SearchContract.GroupView groupView = getView();
        if (groupView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    groupView.showError(strRes);
                }
            });
        }
    }
}
