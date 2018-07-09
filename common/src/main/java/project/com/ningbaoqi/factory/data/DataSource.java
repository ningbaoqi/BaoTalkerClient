package project.com.ningbaoqi.factory.data;

import android.support.annotation.StringRes;

/**
 * 数据源接口定义
 */
public interface DataSource {

    /**
     * 通知包括了成功与失败的回调接口
     *
     * @param <T> 泛型 任意类型
     */
    interface Callback<T> extends SucceedCallback<T>, FailedCallback {

    }

    interface SucceedCallback<T> {//网络请求成功的回调；只关注成功的接口

        void onDataLoaded(T t);
    }

    interface FailedCallback {//网络请求失败的回调;只关注失败的接口

        void onDataNotAvailable(@StringRes int strRes);
    }
}
