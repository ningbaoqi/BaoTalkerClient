package project.com.ningbaoqi.baotalkerclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import project.com.ningbaoqi.common.Common;
import project.com.ningbaoqi.common.app.Activity;

public class MainActivity extends Activity implements IView{
    @BindView(R.id.sample_result)
    TextView mTextView;

    @BindView(R.id.edit_query)
    EditText editText;

    private IPresonter mPresenter;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.btn_submit)
    void onSubmit(){
        mPresenter.submit();
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter = new Persenter(this);
    }

    @Override
    public String getInputString() {
        return editText.getText().toString();
    }

    @Override
    public void setResult(String result) {
        mTextView.setText(result);
    }
}
