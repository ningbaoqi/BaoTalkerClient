package project.com.ningbaoqi.face;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.util.ArrayMap;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 表情工具类
 */
public class Face {

    private static final ArrayMap<String, Bean> FACE_MAP = new ArrayMap<>();//全局的表情的映射，使用ArrayMap更加轻量级
    private static List<FaceTab> FACE_TABS = null;

    private static void init(Context context) {
        if (FACE_TABS == null) {
            synchronized (Face.class) {
                if (FACE_TABS == null) {
                    ArrayList<FaceTab> faceTabs = new ArrayList<>();
                    FaceTab tab = initResourceFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    }
                    tab = initAssetsFace(context);
                    if (tab != null) {
                        faceTabs.add(tab);
                    }
                    for (FaceTab faceTab : faceTabs) {
                        faceTab.copyToMap(FACE_MAP);
                    }
                    FACE_TABS = Collections.unmodifiableList(faceTabs);//不可变集合不可以添加删除
                }
            }
        }
    }

    private static FaceTab initAssetsFace(Context context) {
        return null;
    }

    private static FaceTab initResourceFace(Context context) {//从drawable资源中加载数据，并映射到对应的key
        final ArrayList<Bean> faces = new ArrayList<>();
        final Resources resources = context.getResources();
        String packageName = context.getApplicationInfo().packageName;
        for (int i = 1; i <= 142; i++) {
            String key = String.format(Locale.ENGLISH, "fb%3d", i);
            String resStr = String.format(Locale.ENGLISH, "face_base_%03d", i);
            //根据资源名称去拿资源对应的ID
            int resId = resources.getIdentifier(resStr, "drawable", packageName);
            if (resId == 0) {
                continue;
            }
            faces.add(new Bean(key, resId));//添加表情
        }
        if (faces.size() == 0) {
            return null;
        }
        return new FaceTab("NAME", faces.get(0).preview, faces);
    }

    /**
     * 获取所有的表情
     *
     * @param context
     * @return
     */
    public static List<FaceTab> all(@NonNull Context context) {
        init(context);
        return FACE_TABS;
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
        Object preview;//预览图;包括drawable下面的int类型

        FaceTab(String name, Object preview, List<Bean> faces) {
            this.faces = faces;
            this.name = name;
            this.preview = preview;
        }

        void copyToMap(ArrayMap<String, Bean> faceMap) {//添加到Map中
            for (Bean face : faces) {
                faceMap.put(face.key, face);
            }
        }
    }

    /**
     * 每一个表情
     */
    public static class Bean {
        public String key;
        public Object source;
        public Object preview;
        public String desc;

        Bean(String key, int preview) {
            this.key = key;
            this.preview = preview;
            this.source = preview;
        }
    }
}
