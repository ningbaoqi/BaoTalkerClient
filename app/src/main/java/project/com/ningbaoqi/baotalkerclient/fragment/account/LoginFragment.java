package project.com.ningbaoqi.baotalkerclient.fragment.account;

import android.content.Context;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.app.Fragment;

/**
 * 登陆的Fragment
 */
public class LoginFragment extends Fragment {
    private AccountTrigger mAccountTrigger;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAccountTrigger = (AccountTrigger) context;//拿到我们Activity的引用
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    public void onResume() {
        super.onResume();
//        mAccountTrigger.triggerView();//进行一次切换，默认切换为注册界面
    }
}
