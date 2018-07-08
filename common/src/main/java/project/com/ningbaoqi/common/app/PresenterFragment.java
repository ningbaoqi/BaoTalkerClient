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

    @Override
    public void showError(int str) {
        Application.showToast(str);//显示错误
    }

    @Override
    public void showLoading() {
        // TODO 显示一个 Loading
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
}
