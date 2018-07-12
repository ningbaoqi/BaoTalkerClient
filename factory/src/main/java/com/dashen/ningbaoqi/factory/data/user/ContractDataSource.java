package com.dashen.ningbaoqi.factory.data.user;

import com.dashen.ningbaoqi.factory.model.db.User;

import java.util.List;

import project.com.ningbaoqi.factory.data.DataSource;

/**
 * 联系人数据源
 */
public interface ContractDataSource {

    /**
     * 对数据进行加载的一个职责
     *
     * @param callback 加载成功后返回的callback
     */
    void load(DataSource.SucceedCallback<List<User>> callback);

    /**
     * 销毁操作
     */
    void dispose();

}
