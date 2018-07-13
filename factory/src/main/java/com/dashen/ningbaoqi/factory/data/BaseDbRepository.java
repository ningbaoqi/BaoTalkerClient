package com.dashen.ningbaoqi.factory.data;

import android.support.annotation.NonNull;

import com.dashen.ningbaoqi.factory.data.helper.DBHelper;
import com.dashen.ningbaoqi.factory.model.db.BaseDbModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import project.com.ningbaoqi.factory.data.DbDataSource;
import project.com.ningbaoqi.utils.CollectionUtil;

/**
 * 基础的数据库仓库，实现对数据库的基本监听操作
 */
public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>, DBHelper.ChangedListener<Data>, QueryTransaction.QueryResultListCallback<Data> {
    private SucceedCallback<List<Data>> callback;//和Presenter交互的回调
    private final List<Data> dataList = new LinkedList<>();//当前缓存的数据
    private Class<Data> dataClass;//当前泛型对应的真实的信息

    public BaseDbRepository() {
        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());//拿到当前泛型数组的信息
        dataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SucceedCallback<List<Data>> callback) {
        this.callback = callback;
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        this.callback = null;
        DBHelper.removeChangedListener(dataClass, this);//取消监听
        dataList.clear();
    }

    /**
     * 数据库统一通知的地方：增加/更改
     *
     * @param datas
     */
    @Override
    public void onDataSave(Data... datas) {
        boolean isChanged = false;
        for (Data data : datas) {
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }
        if (isChanged) {
            norifyDataChange();
        }
    }

    protected abstract boolean isRequired(Data data);

    /**
     * 数据库统一通知的地方:删除
     *
     * @param datas
     */
    @Override
    public void onDataDelete(Data... datas) {
        boolean isChanged = false;
        for (Data data : datas) {
            if (dataList.remove(data)) {
                isChanged = true;
            }
        }
        if (isChanged) {
            norifyDataChange();
        }
    }

    /**
     * DBFlow框架通知的回调
     *
     * @param transaction
     * @param tResult
     */
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        if (tResult.size() == 0) {
            dataList.clear();
            norifyDataChange();
            return;
        }
        Data[] users = CollectionUtil.toArray(tResult, dataClass);
        onDataSave(users);//回到数据集更新的操作中
    }

    /**
     * 插入数据
     *
     * @param data
     */
    protected void insert(Data data) {
        dataList.add(data);
    }

    /**
     * 更新操作，更新某个坐标下的数据
     *
     * @param index
     * @param data
     */
    protected void replace(int index, Data data) {
        dataList.remove(index);
        dataList.add(index, data);
    }

    /**
     * 添加数据库的监听操作
     */
    protected void registerDbChangedListener() {
        DBHelper.addChangedListener(dataClass, this);
    }

    /**
     * 通知界面刷新的方法
     */
    private void norifyDataChange() {
        SucceedCallback<List<Data>> callback = this.callback;
        if (callback != null) {
            callback.onDataLoaded(dataList);
        }
    }

    /**
     * 插入或者更新
     *
     * @param data
     */
    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    /**
     * 查询一个数据是否在当前的缓存数据中，如果在则返回坐标
     *
     * @param newData
     * @return
     */
    private int indexOf(Data newData) {
        int index = -1;
        for (Data data : dataList) {
            index++;
            if (data.isSame(newData)) {
                return index;
            }
        }
        return -1;
    }
}
