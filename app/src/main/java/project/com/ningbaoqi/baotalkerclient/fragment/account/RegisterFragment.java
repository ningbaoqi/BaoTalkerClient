package project.com.ningbaoqi.baotalkerclient.fragment.account;

import android.content.Context;
import android.widget.EditText;

import com.dashen.ningbaoqi.factory.presenter.account.RegisterContract;
import com.dashen.ningbaoqi.factory.presenter.account.RegisterPresenter;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.activities.MainActivity;
import project.com.ningbaoqi.common.app.PresenterFragment;

/**
 * 注册的界面
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> implements RegisterContract.View {
    private AccountTrigger mAccountTrigger;
    @BindView(R.id.edit_phone)
    EditText mPhone;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_password)
    EditText mPassword;
    @BindView(R.id.loading)
    Loading mLoading;
    @BindView(R.id.btn_submit)
    Button mSubmit;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = mPhone.getText().toString();
        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        mPresenter.register(phone, name, password);//调用P层进行注册
    }

    @OnClick(R.id.txt_go_login)
    void onShowLoginClick() {
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
        mName.setEnabled(true);
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
        mName.setEnabled(false);
        mPassword.setEnabled(false);
        mSubmit.setEnabled(false);
    }

    /**
     * 注册成功的时候回调；需要进行跳转到MainActivity界面
     */
    @Override
    public void registerSuccess() {
        MainActivity.show(getContext());
        getActivity().finish();//关闭当前界面
    }
}
