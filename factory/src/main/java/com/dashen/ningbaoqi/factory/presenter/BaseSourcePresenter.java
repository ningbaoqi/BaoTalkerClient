package com.dashen.ningbaoqi.factory.presenter;

import java.util.List;

import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.factory.data.DbDataSource;
import project.com.ningbaoqi.factory.presenter.BaseContract;
import project.com.ningbaoqi.factory.presenter.BaseRecyclerPresenter;

/**
 * 基础仓库源的Presenter
 */
public abstract class BaseSourcePresenter<Data, ViewModel, Source extends DbDataSource<Data>, View extends BaseContract.RecyclerView> extends BaseRecyclerPresenter<ViewModel, View> implements DataSource.SucceedCallback<List<Data>> {

    protected Source mSource;


    public BaseSourcePresenter(Source source, View view) {
        super(view);
        this.mSource = source;
    }

    @Override
    public void start() {
        super.start();
        if (mSource != null) {
            mSource.load(this);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        mSource.dispose();
        mSource = null;
    }
}
