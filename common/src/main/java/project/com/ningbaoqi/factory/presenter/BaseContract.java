package project.com.ningbaoqi.factory.presenter;

/**
 * 公共的部分：MVP模式中公共的基本契约
 */
public interface BaseContract {

    interface View<T extends Presenter> {
        void showError(int str);//显示一个字符串错误

        void showLoading();//公共的： 显示一个进度条

        void setPresenter(T presenter);
    }

    interface Presenter {
        void start();//公用的开始方法；初始化操作

        void destroy();//公用的销毁触发
    }
}
