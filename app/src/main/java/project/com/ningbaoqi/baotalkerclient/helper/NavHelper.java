package project.com.ningbaoqi.baotalkerclient.helper;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import project.com.ningbaoqi.common.app.Fragment;

/**
 * 完成对Fragment的调度与重用问题，达到最优的Fragment切换
 */
public class NavHelper<T> {
    private final FragmentManager fragmentManager;
    private final int containerID;
    private final SparseArray<Tab<T>> tabs = new SparseArray();//所有的Tab集合
    private final Context context;
    private final OnTabChangedListener<T> listener;
    private Tab<T> currentTab;//当前的一个选中的Tab

    public NavHelper(Context context, int containerID, FragmentManager fragmentManager, OnTabChangedListener<T> listener) {
        this.fragmentManager = fragmentManager;
        this.containerID = containerID;
        this.context = context;
        this.listener = listener;
    }

    /**
     * 用来添加Tab
     *
     * @param menuID 对应的菜单ID
     * @param tab    Tab
     */
    public NavHelper<T> add(int menuID, Tab<T> tab) {
        tabs.put(menuID, tab);
        return this;
    }

    /**
     * 获取当前显示的Tab
     *
     * @return 当前的Tab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     *
     * @param menuID 菜单的ID
     * @return 是否能够处理这个点击
     */
    public boolean performClickMenu(int menuID) {
        Tab<T> tab = tabs.get(menuID);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真实的Tab选择操作
     *
     * @param tab Tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {//当前Tab就是点击的Tab，进行刷新
                notifyTabReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * 进行Fragment的真实的调度操作
     *
     * @param newTab 新的
     * @param oldTab 旧的
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                transaction.detach(oldTab.fragment);//解除Fragment与对应的Activity的绑定，但是还在Fragment的缓存缓存空间中
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {//创建首次新建，并缓存起来
                Fragment fragment = (Fragment) Fragment.instantiate(context, newTab.clx.getName(), null);
                newTab.fragment = fragment;
                transaction.add(containerID, fragment, newTab.clx.getName());//提交到FragmentManager
            } else {
                transaction.attach(newTab.fragment);//从FragmentManager的缓存空间中重新加载到界面中
            }
        }
        transaction.commit();
        notifyTabSelect(newTab, oldTab);//通知回调
    }

    /**
     * 回调我们的监听器
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChanged(newTab, oldTab);
        }
    }

    /**
     * 二次点击刷新
     *
     * @param tab
     */
    private void notifyTabReselect(Tab<T> tab) {
        // TODO 二次点击Tab所作的操作
    }


    /**
     * 我们的所有的Tab基础属性
     *
     * @param <T> 泛型的额外参数
     */
    public static class Tab<T> {
        public Class<? extends Fragment> clx;//Fragment对应的class 信息
        public T extra;//额外的字段，用户自己设定需要什么东西
        Fragment fragment;//内部缓存的对应的Fragment对象

        public Tab(Class<? extends Fragment> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }
    }

    /**
     * 定义事件处理完成后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
