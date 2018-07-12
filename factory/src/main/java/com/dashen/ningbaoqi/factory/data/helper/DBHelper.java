package com.dashen.ningbaoqi.factory.data.helper;

import com.dashen.ningbaoqi.factory.model.db.AppDatabase;
import com.dashen.ningbaoqi.factory.model.db.GroupMember;
import com.dashen.ningbaoqi.factory.model.db.Message;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();//观察者集合：观察的表：每一个表的观察者有很多

    /**
     * 从所有的监听器中，获取某一个表的所有监听者
     *
     * @param modelClass 表对应的Class信息
     * @param <Model>    泛型
     * @return Set<ChangedListener>
     */
    private <Model extends BaseModel> Set<ChangedListener> getListeners(Class<Model> modelClass) {
        if (changedListeners.containsKey(modelClass)) {
            return changedListeners.get(modelClass);
        }
        return null;
    }

    /**
     * 添加一个监听
     *
     * @param tClass   对某个表关注
     * @param listener 监听者
     * @param <Model>  表的泛型
     */
    public static <Model extends BaseModel> void addChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            changedListeners = new HashSet<>();//初始化某一类型的容器
            instance.changedListeners.put(tClass, changedListeners);//添加到总的Map
        }
        changedListeners.add(listener);
    }

    /**
     * 删除指定表的监听器
     *
     * @param tClass   表的Class信息
     * @param listener 监听器
     * @param <Model>  表的泛型
     */
    public static <Model extends BaseModel> void removeChangedListener(final Class<Model> tClass, ChangedListener<Model> listener) {
        Set<ChangedListener> changedListeners = instance.getListeners(tClass);
        if (changedListeners == null) {
            return;
        }
        changedListeners.remove(listener);
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
        final Set<ChangedListener> listeners = getListeners(tClass);
        if (listeners != null && listeners.size() > 0) {
            for (ChangedListener<Model> listener : listeners) {//通用的通知
                listener.onDataSave(models);
            }
        }

        if (GroupMember.class.equals(tClass)) { //例外情况：群成员变更需要通知对应的群信息更新
            updateGroup((GroupMember[]) models);
        } else if (Message.class.equals(tClass)) {//例外情况：消息变化应该通知会话列表更新
            updateSession((Message[]) models);
        }
    }

    /**
     * 从成员中找出成员对应的群，并对群进行更新
     *
     * @param members 群成员列表
     */
    private void updateGroup(GroupMember... members) {

    }

    /**
     * 从消息列表中筛选中对应的回话，并对会话进行更新
     *
     * @param messages
     */
    private void updateSession(Message... messages) {

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

    /**
     * 通知监听器
     */
    public interface ChangedListener<Data> {

        void onDataSave(Data... datas);

        void onDataDelete(Data... datas);

    }
}
