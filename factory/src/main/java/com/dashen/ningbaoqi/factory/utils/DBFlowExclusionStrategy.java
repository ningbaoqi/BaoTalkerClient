package com.dashen.ningbaoqi.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/**
 * DBFlow的数据库过滤字段
 */
public class DBFlowExclusionStrategy implements ExclusionStrategy {
    /**
     * 被跳过的字段
     *
     * @param f
     * @return
     */
    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaredClass().equals(ModelAdapter.class);//只要是属于DBFlow的数据
    }

    /**
     * 被跳过的Class
     *
     * @param clazz
     * @return
     */
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
