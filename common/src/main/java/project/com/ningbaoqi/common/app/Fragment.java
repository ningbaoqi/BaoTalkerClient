package project.com.ningbaoqi.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author ningbaoqi
 * */
public abstract class Fragment extends android.support.v4.app.Fragment{
    protected View mRoot ;//复用问题
    protected Unbinder mRootUnBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 如果参数正确返回true，错误返回false
     */
    protected void initArgs(Bundle bundle){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null){
            int layId = getContentLayoutId();
            View root = inflater.inflate(layId , container , false);//初始化当前的根布局，但是不在创建时就添加到container里面去，在return mRoot的时候，在Fragment的内部调用的时候，调度之后添加到container里面去
            initWidget(root);
            mRoot = root;
        }else {
            if (mRoot.getParent()!= null){//判断该Rootview的父布局等不等于空 ，如果不等于空，将mRoot在    在调度之后在mRoot父布局中已经有了mRoot，如果Fragment被回收，重新初始化这个Fragment的时候，有可能mRoot还没有被回收
                ((ViewGroup)mRoot.getParent()).removeView(mRoot);//把当前Root从其父控件中移除
            }
        }
        return mRoot;
    }

    /**
     * 当界面初始化完成之后初始化数据
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    /**
     * 得到当前界面的资源文件Id
     * @return 资源ID
     */
    protected abstract int getContentLayoutId();

    /**
     *
     */
    protected void initWindow(){

    }

    /**
     * 初始化控件
     * @param root
     */
    protected void initWidget(View root){
        mRootUnBinder = ButterKnife.bind(this , root);
    }

    /**
     * 初始化数据
     */
    protected void initData(){


    }


    /**
     * 返回按键触发时调用
     * @return true 代表我已处理返回逻辑，Activity不用关心不用finish自己了；false 表示我没有处理，acitivty自己走自己的逻辑
     */
    public boolean onBackPress(){
        return false;
    }
}
