package com.dashen.ningbaoqi.factory.data.helper;

import com.dashen.ningbaoqi.factory.model.db.Session;
import com.dashen.ningbaoqi.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * 会话辅助工具类
 */
public class SessionHelper {

    /**
     * 从本地找到Session
     *
     * @param id
     * @return
     */
    public static Session findFromLocal(String id) {
        return SQLite.select().from(Session.class).where(Session_Table.id.eq(id)).querySingle();
    }
}
