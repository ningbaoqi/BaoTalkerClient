package com.dashen.ningbaoqi.factory.presenter.search;

import com.dashen.ningbaoqi.factory.data.helper.UserHelper;
import com.dashen.ningbaoqi.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.factory.presenter.BasePresenter;
import retrofit2.Call;

/**
 * 搜索人的实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView> implements SearchContract.Presenter, DataSource.Callback<List<UserCard>> {
    private Call searchCall;

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        if (call != null && !call.isCanceled()) {//如果有上一次的请求并且没有取消则调用取消操作
            call.cancel();
        }
        searchCall = UserHelper.search(content, this);
    }

    /**
     * 搜索成功
     *
     * @param userCards
     */
    @Override
    public void onDataLoaded(final List<UserCard> userCards) {
        final SearchContract.UserView userView = getView();
        if (userView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    userView.onSearchDone(userCards);
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
        final SearchContract.UserView userView = getView();
        if (userView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    userView.showError(strRes);
                }
            });
        }
    }
}
