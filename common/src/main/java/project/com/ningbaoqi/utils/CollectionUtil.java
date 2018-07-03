package project.com.ningbaoqi.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 集合工具类
 */
public class CollectionUtil {

    /**
     * List集合转换为数组
     *
     * @param items List数据
     * @param clazz 数据的类型class
     * @param <T>   Class
     * @return 转换完成后的数组
     */
    public static <T> T[] toArray(List<T> items, Class<T> clazz) {
        if (items == null || items.size() == 0) {
            return null;
        }
        int size = items.size();
        try {
            T[] array = (T[]) Array.newInstance(clazz, size);
            return items.toArray(array);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Set集合转换为数组
     *
     * @param items
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T[] toArray(Set<T> items, Class<T> clazz) {
        if (items == null || items.size() == 0) {
            return null;
        }
        int size = items.size();
        try {
            T[] array = (T[]) Array.newInstance(clazz, size);
            return items.toArray(array);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 数组集合转换为HashSet集合
     *
     * @param items
     * @param <T>
     * @return
     */
    public static <T> HashSet<T> toHashSet(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        HashSet<T> set = new HashSet<>();
        Collections.addAll(set, items);
        return set;
    }

    /**
     * 数组转换成为ArrayList集合
     *
     * @param items
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> toArrayList(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        ArrayList<T> list = new ArrayList<>();
        Collections.addAll(list, items);
        return list;
    }
}
