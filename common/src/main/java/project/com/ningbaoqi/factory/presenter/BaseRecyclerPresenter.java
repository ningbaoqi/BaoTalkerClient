package project.com.ningbaoqi.factory.presenter;

import android.support.v7.util.DiffUtil;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;

/**
 * 对RecyclerView进行的一个简单的Presenter封装
 *
 * @param <View>
 */
public class BaseRecyclerPresenter<ViewMode, View extends BaseContract.RecyclerView> extends BasePresenter<View> {

    public BaseRecyclerPresenter(View view) {
        super(view);
    }

    /**
     * 刷新一堆新数据到界面中
     *
     * @param dataList 新数据
     */
    protected void refreshData(final List<ViewMode> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view == null) {
                    return;
                }
                //基本的更新数据并刷新界面
                RecyclerAdapter<ViewMode> adapter = view.getRecyclerAdapyer();
                adapter.replace(dataList);
                view.onAdapterDataChanged();
            }
        });
    }

    /**
     * 刷新界面操作，该操作可以保证执行方法在主线程执行
     *
     * @param diffResult 一个差异的结果集
     * @param dataList   具体的新数据
     */
    protected void refreshData(final DiffUtil.DiffResult diffResult, final List<ViewMode> dataList) {
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                refreshDataOnUiThread(diffResult, dataList);
            }
        });
    }

    private void refreshDataOnUiThread(final DiffUtil.DiffResult diffResult, final List<ViewMode> dataList) {
        View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<ViewMode> adapter = view.getRecyclerAdapyer();
        adapter.getItems().clear();//改变数据集合而不通知界面刷新
        adapter.getItems().addAll(dataList);
        view.onAdapterDataChanged();//通知界面刷新占位布局
        diffResult.dispatchUpdatesTo(adapter);//进行增量更新
    }
}
