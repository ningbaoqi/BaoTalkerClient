package com.dashen.ningbaoqi.factory.model.db.view;

import com.dashen.ningbaoqi.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * 群成员对应的用户的简单信息表
 */
@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId;//User id 或者 Member userId
    @Column
    public String name;//User表中的name
    @Column
    public String alias;//Member alias
    @Column
    public String portrait;//User - portrait
}
