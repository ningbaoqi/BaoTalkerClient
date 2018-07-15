package project.com.ningbaoqi.factory.presenter;

import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;

/**
 * 公共的部分：MVP模式中公共的基本契约
 */
public interface BaseContract {

    /**
     * 基本的界面职责
     *
     * @param <T>
     */
    interface View<T extends Presenter> {
        void showError(int str);//显示一个字符串错误

        void showLoading();//公共的： 显示一个进度条

        void setPresenter(T presenter);
    }

    /**
     * 基本的职责
     */
    interface Presenter {
        void start();//公用的开始方法；初始化操作

        void destroy();//公用的销毁触发
    }

    /**
     * 基本的列表的View的职责
     *
     * @param <T>
     */
    interface RecyclerView<T extends Presenter, ViewMode> extends View<T> {
        RecyclerAdapter<ViewMode> getRecyclerAdapter();

        void onAdapterDataChanged();//当适配器数据更改的时候进行触发
    }
}
