package com.dashen.ningbaoqi.factory.utils;

import android.support.v7.util.DiffUtil;

import java.util.List;

public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {

    private List<T> mOldList, mNewList;

    public DiffUiDataCallback(List<T> mOldList, List<T> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    /**
     * 旧的数据大小
     *
     * @return
     */
    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    /**
     * 新的数据大小
     *
     * @return
     */
    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    /**
     * 两个类是不是同一个东西
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isSame(beanOld);
    }

    /**
     * 在经过相等判断后，进一步判断是否有数据更改
     *
     * @param oldItemPosition
     * @param newItemPosition
     * @return
     */
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T beanOld = mOldList.get(oldItemPosition);
        T beanNew = mNewList.get(newItemPosition);
        return beanNew.isUiContentSame(beanOld);
    }

    /**
     * 进行实际的比较的数据类型
     *
     * @param <T>
     */
    public interface UiDataDiffer<T> {
        boolean isSame(T old);//传递一个旧的数据给你，问你是否和你表示的是同一个数据

        boolean isUiContentSame(T old);//你和旧的数据对比，内容是否相同
    }
}
