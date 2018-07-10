package com.dashen.ningbaoqi.factory.presenter.search;

import project.com.ningbaoqi.factory.presenter.BasePresenter;

/**
 * 搜索群的实现
 */
public class SearchGroupPresenter extends BasePresenter<SearchContract.GroupView> implements SearchContract.Presenter {

    public SearchGroupPresenter(SearchContract.GroupView view) {
        super(view);
    }

    @Override
    public void search(String content) {

    }
}
