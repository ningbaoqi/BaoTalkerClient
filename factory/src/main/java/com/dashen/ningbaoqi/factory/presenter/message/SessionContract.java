package com.dashen.ningbaoqi.factory.presenter.message;

import com.dashen.ningbaoqi.factory.model.db.Session;

import project.com.ningbaoqi.factory.presenter.BaseContract;

public interface SessionContract {
    interface Presenter extends BaseContract.Presenter {

    }

    interface View extends BaseContract.RecyclerView<Presenter, Session> {

    }
}
