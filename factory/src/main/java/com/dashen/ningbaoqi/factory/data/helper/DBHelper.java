package com.dashen.ningbaoqi.factory.data.helper;

import com.dashen.ningbaoqi.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;

/**
 * 数据库的辅助工具类：辅助完成增删改
 */
public class DBHelper {
    private static final DBHelper instance;


    private DBHelper() {
    }

    static {
        instance = new DBHelper();
    }

    /**
     * 数据库保存的封装：新增或者修改的统一方法
     *
     * @param tClass  保存的Class队形
     * @param models  数据
     * @param <Model> 数据类型
     */
    public static <Model extends BaseModel> void save(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);//当前数据库的一个管理者
        definition.beginTransactionAsync(new ITransaction() {//提交一个事物
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.saveAll(Arrays.asList(models));
                instance.notifySave(tClass, models);//保存成功将调用通知方法
            }
        }).build().execute();
    }

    /**
     * 数据库删除的封装
     *
     * @param tClass  删除的Class类型
     * @param models  数据
     * @param <Model> 数据类型
     */
    public static <Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models) {
        if (models == null || models.length == 0) {
            return;
        }
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);//当前数据库的一个管理者
        definition.beginTransactionAsync(new ITransaction() {//提交一个事物
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Model> adapter = FlowManager.getModelAdapter(tClass);
                adapter.deleteAll(Arrays.asList(models));
                instance.notifyDelete(tClass, models);//删除成功将调用通知方法
            }
        }).build().execute();
    }

    /**
     * 通知保存
     *
     * @param tClass  通知的类型
     * @param models  数据
     * @param <Model> 数据类型
     */
    private final <Model extends BaseModel> void notifySave(final Class<Model> tClass, final Model... models) {
        // TODO
    }

    /**
     * 通知删除
     *
     * @param tClass  通知的类型
     * @param models  数据
     * @param <Model> 数据类型
     */
    private final <Model extends BaseModel> void notifyDelete(final Class<Model> tClass, final Model... models) {
        // TODO
    }
}
