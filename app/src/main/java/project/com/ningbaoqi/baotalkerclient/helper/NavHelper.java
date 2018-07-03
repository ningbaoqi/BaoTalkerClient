package project.com.ningbaoqi.baotalkerclient.helper;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import project.com.ningbaoqi.common.app.Fragment;

/**
 * 完成对Fragment调度和重用问题，达到最优的Fragment切换
 */
public class NavHelper<T> {
    private final Context context;
    private final int containerID;//容器ID
    private final FragmentManager fragmentManager;//唯一的FragmentManager
    private final OnTabChangedListener<T> listener;
    private final SparseArray<Tab<T>> tabs = new SparseArray();//菜单容器，因为其他的容器太过于厚重，这个轻量些
    private Tab<T> currentTab;//当前选中的Tab


    public NavHelper(Context context, int containerID, FragmentManager fragmentManager, OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerID = containerID;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    /**
     * 添加Tab
     *
     * @param menuID
     * @param tab
     * @return 当前对象，用于流式添加
     */
    public NavHelper<T> addTab(int menuID, Tab<T> tab) {
        tabs.put(menuID, tab);
        return this;
    }

    /**
     * 获取当前显示的TAB
     *
     * @return
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     *
     * @param itemId 菜单的ID
     * @return 是否能够处理点击
     */
    public boolean performClickMenu(int itemId) {
        Tab<T> tab = tabs.get(itemId);//集合中寻找点击的菜单对应的Tab
        if (tab != null) {//如果有则进行处理
            doSelect(tab);
            return true;
        }
        return false;
    }

    /**
     * 进行真实的item选择操作
     *
     * @param tab
     */
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {//如果当前的tab就是点击的tab就不做处理
                notifyTabReselect(tab);//二次点击所作的操作
                return;
            }
        }
        //赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    /**
     * 进行Fragmrent的真实的调度
     *
     * @param newTab
     * @param oldTab
     */
    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                ft.detach(oldTab.fragment);//将Fragment先和界面解固定，不显示在界面上了，但是还在内存中
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {//第一次点击的时候等于空就创建一个fragment
                Fragment fragment = (Fragment) Fragment.instantiate(context, newTab.clx.getName(), null);
                newTab.fragment = fragment;
                ft.add(containerID, fragment, newTab.clx.getName());//提交到FragmentManager
            } else {
                ft.attach(newTab.fragment);//从FragmentManager管理的内存中添加回来，并不需要重新创建
            }
        }
        ft.commit();//提交事务
        notifyTabSelect(newTab, oldTab);//通知回调
    }

    /**
     * 通知界面刷新了
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        // TODO 通知界面刷新了
        if (listener != null) {
            listener.onTabChanged(newTab, oldTab);
        }
    }

    /**
     * 二次点击操作同一个Tab所作的操作
     *
     * @param tab
     */
    private void notifyTabReselect(Tab<T> tab) {
        // TODO 二次点击操作同一个Tab所作的操作
    }

    /**
     * 不能让该类引用helper，不能循环引用，所以设置为static；
     * 菜单类:所有的Tab基础属性
     */
    public static class Tab<T> {
        public Class<? extends Fragment> clx;//Fragment对应的class信息
        public T extra;//用户自己设定需要什么东西
        Fragment fragment;//内部缓存的对应的Fragment，包权限

        public Tab(Class<? extends Fragment> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }
    }

    /**
     * 定义事件处理完成后的接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
