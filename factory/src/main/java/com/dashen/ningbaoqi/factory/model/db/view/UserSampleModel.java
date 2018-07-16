package com.dashen.ningbaoqi.factory.model.db.view;

import com.dashen.ningbaoqi.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

import project.com.ningbaoqi.factory.model.Author;

/**
 * 用户的基本信息model，可以和数据库进行查询
 */
@QueryModel(database = AppDatabase.class)
public class UserSampleModel implements Author{
    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String portrait;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {

    }

    @Override
    public String getPortrait() {
        return null;
    }

    @Override
    public void setPortrait(String portrait) {

    }
}
