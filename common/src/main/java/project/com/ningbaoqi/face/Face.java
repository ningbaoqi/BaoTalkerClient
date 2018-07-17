package project.com.ningbaoqi.face;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.util.ArrayMap;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情工具类
 */
public class Face {

    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();//全局的表情的映射，使用ArrayMap更加轻量级

    /**
     * 获取所有的表情
     *
     * @param context
     * @return
     */
    public static List<FaceTab> all(@NonNull Context context) {
        return null;
    }

    /**
     * 输入表情到Editable
     *
     * @param context
     * @param editable
     * @param bean
     * @param size
     */
    public static void inputFace(@NonNull Context context, final Editable editable, final Face.Bean bean, int size) {

    }

    /**
     * 从Spannable解析表情并替换显示
     *
     * @param target
     * @param spannable
     * @param size
     * @return
     */
    public static List<FaceTab> decode(@NonNull View target, final Spannable spannable, final int size) {
        return null;
    }

    /**
     * 每一个表情盘含有很多表情
     */
    public static class FaceTab {
        public List<Bean> faces = new ArrayList<>();
        public String name;
        public Object preview;//预览图;包括drawable下面的int类型
    }

    /**
     * 每一个表情
     */
    public static class Bean {
        public static String key;
        public static Object source;
        public static Object preview;
        public static String desc;
    }
}
