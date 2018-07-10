package com.dashen.ningbaoqi.factory.presenter.search;

import project.com.ningbaoqi.factory.presenter.BasePresenter;

/**
 * 搜索人的实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContract.UserView> implements SearchContract.Presenter {

    public SearchUserPresenter(SearchContract.UserView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
