package com.dashen.ningbaoqi.factory.presenter.search;

import com.dashen.ningbaoqi.factory.model.card.GroupCard;
import com.dashen.ningbaoqi.factory.model.card.UserCard;

import java.util.List;

import project.com.ningbaoqi.factory.presenter.BaseContract;

public interface SearchContract {

    public interface Presenter extends BaseContract.Presenter {

        void search(String content);//搜索内容

    }

    public interface UserView extends BaseContract.View<Presenter> {//搜索人的界面

        void onSearchDone(List<UserCard> userCards);
    }

    public interface GroupView extends BaseContract.View<Presenter> {//搜索群的界面

        void onSearchDone(List<GroupCard> groupCards);

    }
}
