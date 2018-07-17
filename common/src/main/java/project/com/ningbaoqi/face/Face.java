package project.com.ningbaoqi.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import project.com.ningbaoqi.utils.StreamUtil;

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

    /**
     * 从face-t.zip包解析我们的表情
     *
     * @param context
     * @return
     */
    private static FaceTab initAssetsFace(Context context) {
        String faceAsset = "face-t.zip";
        String faceCacheDir = String.format("%s/face/tf", context.getFilesDir());//data/data/包名/files/face/tf/*
        File faceFolder = new File(faceCacheDir);
        if (!faceFolder.exists()) {
            if (faceFolder.mkdirs()) {
                try {
                    InputStream inputStream = context.getAssets().open(faceAsset);
                    //存储的文件
                    File faceSource = new File(faceFolder, "source.zip");
                    StreamUtil.copy(inputStream, faceSource);
                    //解压
                    unZipFile(faceSource, faceFolder);
                    //清理文件
                    StreamUtil.delete(faceSource.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //找到info.json文件
        File infoFile = new File(faceCacheDir, "info.json");
        //Gson解析
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = gson.newJsonReader(new FileReader(infoFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        FaceTab tab = gson.fromJson(reader, FaceTab.class);//解析
        for (Bean face : tab.faces) {//相对路径到绝对路径
            face.preview = String.format("%s/%s", faceCacheDir, face.preview);
            face.source = String.format("%s/%s", faceCacheDir, face.source);
        }
        return tab;
    }

    /**
     * 解压文件
     *
     * @param zipFile
     * @param desDir
     */
    private static void unZipFile(File zipFile, File desDir) throws IOException {
        final String folderPath = desDir.getAbsolutePath();
        ZipFile zf = new ZipFile(zipFile);
        //判断节点进行循环
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            //过滤缓存文件
            String name = entry.getName();
            if (name.startsWith(".")) {
                continue;
            }
            //输入流
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + name;
            //防止名字错乱
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            //输出文件
            StreamUtil.copy(in, desFile);
        }
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
    public static void inputFace(@NonNull final Context context, final Editable editable, final Face.Bean bean, int size) {
        Glide.with(context).load(bean.preview).asBitmap().into(new SimpleTarget<Bitmap>(size, size) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Spannable spannable = new SpannableString(String.format("[%s]", bean.key));
                ImageSpan span = new ImageSpan(context, resource, ImageSpan.ALIGN_BASELINE);
                spannable.setSpan(span, 0, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//属性为前后不关联
                editable.append(spannable);
            }
        });
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
