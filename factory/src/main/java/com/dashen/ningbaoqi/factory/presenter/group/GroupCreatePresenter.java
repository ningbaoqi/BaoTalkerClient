package com.dashen.ningbaoqi.factory.presenter.group;

import android.text.TextUtils;

import com.dashen.ningbaoqi.factory.Factory;
import com.dashen.ningbaoqi.factory.R;
import com.dashen.ningbaoqi.factory.data.helper.GroupHelper;
import com.dashen.ningbaoqi.factory.data.helper.UserHelper;
import com.dashen.ningbaoqi.factory.model.api.group.GroupCreateModel;
import com.dashen.ningbaoqi.factory.model.card.GroupCard;
import com.dashen.ningbaoqi.factory.model.db.view.UserSampleModel;
import com.dashen.ningbaoqi.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project.com.ningbaoqi.factory.data.DataSource;
import project.com.ningbaoqi.factory.presenter.BaseRecyclerPresenter;

public class GroupCreatePresenter extends BaseRecyclerPresenter<GroupCreateContract.ViewModel, GroupCreateContract.View> implements GroupCreateContract.Presenter, DataSource.Callback<GroupCard> {
    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void start() {
        super.start();
        Factory.runOnAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        GroupCreateContract.View view = getView();
        view.showLoading();
        //判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(picture) || users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
        }
        //上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url)) {
                    return;
                }
                GroupCreateModel model = new GroupCreateModel(name, desc, url, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });
    }

    /**
     * 上传头像
     *
     * @param path
     * @return
     */
    private String uploadPicture(String path) {
        String url = UploadHelper.uploadPortrait(path);
        if (TextUtils.isEmpty(url)) {
            Run.onUiAsync(new Action() {//切换到主线程 提示信息
                @Override
                public void call() {
                    GroupCreateContract.View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }

    @Override
    public void changeSelect(GroupCreateContract.ViewModel model, boolean isSelected) {
        if (isSelected) {
            users.add(model.author.getId());
        } else {
            users.remove(model.author.getId());
        }
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSampleContact();
            List<GroupCreateContract.ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                GroupCreateContract.ViewModel viewModel = new GroupCreateContract.ViewModel();
                viewModel.author = sampleModel;
                models.add(viewModel);
            }
            refreshData(models);
        }
    };

    /**
     * 成功了:回调
     *
     * @param groupCard
     */
    @Override
    public void onDataLoaded(GroupCard groupCard) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.onCreateSucceed();
                }
            }
        });
    }

    /**
     * 失败情况下 ,回调
     *
     * @param strRes
     */
    @Override
    public void onDataNotAvailable(final int strRes) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                GroupCreateContract.View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });
    }
}
