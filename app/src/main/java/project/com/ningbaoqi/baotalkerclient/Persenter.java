package project.com.ningbaoqi.baotalkerclient;

import android.text.TextUtils;
import android.widget.TextView;

public class Persenter implements IPresonter{
    private IView mIview;
    public Persenter(IView iView){
        this.mIview = iView;
    }
    @Override
    public void submit() {
        //开启LOADING
        String inputString = mIview.getInputString();
        if (TextUtils.isEmpty(inputString)){
            return;
        }
        IUserService service = new UserService();
        String result = "resultL" + service.search(inputString.hashCode());
        //关闭界面LOADING
        mIview.setResult(result);
    }
}
