package com.dashen.ningbaoqi.factory.presenter.contact;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.data.helper.UserHelper;
import com.dashen.ningbaoqi.factory.model.db.User;
import com.dashen.ningbaoqi.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import project.com.ningbaoqi.factory.presenter.BasePresenter;

public class PersonalPresenter extends BasePresenter<PersonalContract.View> implements PersonalContract.Presenter {
    private User user;

    public PersonalPresenter(PersonalContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        //个人界面用户数据优先从网络拉取
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                PersonalContract.View view = getView();
                if (view != null) {
                    String id = view.getUserId();
                    User user = UserHelper.searchFirstOfNet(id);
                    onLoaded(view, user);
                }
            }
        });
    }


    private void onLoaded(final PersonalContract.View view, final User user) {
        this.user = user;
        final boolean isSelf = user.getId().equalsIgnoreCase(Account.getUserId());//是否是我自己
        final boolean isFollow = isSelf || user.isFollow();//是否已经关注
        final boolean allowSatHello = isFollow && !isSelf;//已经关注，并且同时不是自己才能聊天
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.onLoadDone(user);
                view.setFollowStatus(isFollow);
                view.allowSayHello(allowSatHello);
            }
        });
    }


    @Override
    public User getUserPersonal() {
        return user;
    }
}
