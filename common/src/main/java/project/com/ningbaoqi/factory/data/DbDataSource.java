package project.com.ningbaoqi.factory.data;

import java.util.List;

/**
 * 基础的数据库数据源接口定义
 *
 * @param <Data>
 */
public interface DbDataSource<Data> extends DataSource {
    /**
     * 有一个基本的数据源加载方法，传递一个callback回调，一般回调到Presenter
     *
     * @param callback 加载成功后返回的callback
     */
    void load(DataSource.SucceedCallback<List<Data>> callback);
}
