package project.com.ningbaoqi.common.app;


import android.app.ProgressDialog;
import android.content.DialogInterface;

import project.com.ningbaoqi.common.R;
import project.com.ningbaoqi.factory.presenter.BaseContract;

public abstract class PresenterToolbarActivity<Presenter extends BaseContract.Presenter> extends ToolbarActivity implements BaseContract.View<Presenter> {
    protected Presenter mPresenter;
    protected ProgressDialog mLoadingDialog;

    @Override
    protected void initBefore() {
        super.initBefore();
        initPresenter();//初始化Presenter
    }

    protected abstract Presenter initPresenter();

    @Override
    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void showError(int str) {
        hideDialogLoading();
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerError(str);
        } else {
            Application.showToast(str);
        }
    }

    @Override
    public void showLoading() {
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerLoading();
        } else {
            ProgressDialog dialog = mLoadingDialog;
            if (dialog == null) {
                dialog = new ProgressDialog(this, R.style.AppTheme_Dialog_Alert_Light);
                dialog.setCanceledOnTouchOutside(false);//不可触摸取消
                dialog.setCancelable(true);//强制取消就关闭界面
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });
                mLoadingDialog = dialog;
            }
            dialog.setMessage(getText(R.string.prompt_loading));
            dialog.show();
        }
    }

    protected void hideLoading() {
        hideDialogLoading();
        if (mPlaceHolderView != null) {
            mPlaceHolderView.triggerOk();
        }
    }

    /**
     * 隐藏dialog loading
     */
    protected void hideDialogLoading() {
        ProgressDialog dialog = mLoadingDialog;
        if (dialog != null) {
            mLoadingDialog = null;
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }
}
