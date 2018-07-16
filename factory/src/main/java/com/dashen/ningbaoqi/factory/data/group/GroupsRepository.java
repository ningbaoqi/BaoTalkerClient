package com.dashen.ningbaoqi.factory.data.group;

import com.dashen.ningbaoqi.factory.data.BaseDbRepository;
import com.dashen.ningbaoqi.factory.model.db.Group;

/**
 * 我的群组的数据仓库
 */
public class GroupsRepository extends BaseDbRepository<Group> implements GroupsDataSource {

    @Override
    protected boolean isRequired(Group group) {//所有的群我都关注
        return true;
    }
}
