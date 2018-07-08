package project.com.ningbaoqi.factory.presenter;

public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    private T mView;

    public BasePresenter(T view) {
        setView(view);
    }

    /**
     * 设置一个View，子类可以复写
     *
     * @param view 返回View
     */
    @SuppressWarnings("unchecked")
    protected void setView(T view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    /**
     * 给子类使用的获取View的操作；不允许重写
     *
     * @return 返回mView
     */
    protected final T getView() {
        return mView;
    }

    /**
     * 开始的时候进行Loading调用
     */
    @Override
    public void start() {
        T view = mView;
        if (view != null) {
            view.showLoading();
        }
    }

    /**
     * 销毁的时候逻辑
     */
    @SuppressWarnings("unchecked")
    @Override
    public void destroy() {
        T view = mView;
        mView = null;
        if (view != null) {
            view.setPresenter(null);//把presenter设置为null
        }
    }
}
