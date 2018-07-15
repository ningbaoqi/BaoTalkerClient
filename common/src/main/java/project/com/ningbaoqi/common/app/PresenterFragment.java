package project.com.ningbaoqi.common.app;

import android.content.Context;

import project.com.ningbaoqi.factory.presenter.BaseContract;

public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends Fragment implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;

    protected abstract Presenter initPresenter();//初始化Presenter返回一个泛型的Presenter

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();//在界面onAttach的时候触发初始化Presenter
    }

    /**
     * //显示错误 优先使用占位布局
     *
     * @param str
     */
    @Override
    public void showError(int str) {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
            return;
        } else {
            Application.showToast(str);
        }
    }

    /**
     * 显示Loading状态
     */
    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        }
    }

    /**
     * View中赋值Presenter
     *
     * @param presenter
     */
    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
