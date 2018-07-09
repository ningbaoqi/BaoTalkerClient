package project.com.ningbaoqi.baotalkerclient.fragment.account;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import com.dashen.ningbaoqi.factory.presenter.account.LoginContract;
import com.dashen.ningbaoqi.factory.presenter.account.LoginPresenter;

import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.MainActivity;
import project.com.ningbaoqi.common.app.Fragment;
import project.com.ningbaoqi.common.app.PresenterFragment;

/**
 * 登陆的Fragment
 */
public class LoginFragment extends PresenterFragment<LoginContract.Presenter> implements LoginContract.View {
    private AccountTrigger mAccountTrigger;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    @Override
    protected LoginContract.Presenter initPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;//拿到我们Activity的引用
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.login(phone, password);
    }

    @OnClick(R.id.txt_go_register)
    void onShowRegisterClick() {
        mAccountTrigger.triggerView();//让Account进行界面切换
    }

    /**
     * 注册错误的时候调用
     *
     * @param str
     */
    @Override
    public void showError(int str) {
        super.showError(str);
        //当提示需要显示错误的时候触发；一定是结束了
        mLoading.stop();//停止Loading
        mPhone.setEnabled(true);//让控件可以输入
        mPassword.setEnabled(true);
        mSubmit.setEnabled(true);//提交按钮可以继续点击
    }

    /**
     * 正在加载的时候回调
     */
    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行时，界面不可操作
        mLoading.start();//开始Loading
        mPhone.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    @Override
    public void loginSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();
    }
}
