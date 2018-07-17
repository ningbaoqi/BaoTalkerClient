package project.com.ningbaoqi.face;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import project.com.ningbaoqi.common.R;
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
    public static Spannable decode(@NonNull View target, final Spannable spannable, final int size) {
        if (spannable == null) {
            return null;
        }
        String str = spannable.toString();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        final Context context = target.getContext();
        Pattern pattern = Pattern.compile("(\\[[^\\[\\]:\\s\\n]+\\])");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String key = matcher.group();//[ft112]
            if (TextUtils.isEmpty(key)) {
                continue;
            }
            Bean bean = get(context, key.replace("[", "").replace("]", ""));
            if (bean == null) {
                continue;
            }
            final int start = matcher.start();
            final int end = matcher.end();
            ImageSpan span = new FaceSpan(context, target, bean.preview, size);//得到一个复写后的span
            //设置标示
            spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static class FaceSpan extends ImageSpan {
        private Drawable mDrawable;//自己真实绘制的
        private View mView;
        private int mSize;

        /**
         * @param context 上下文
         * @param view    目标View；用于刷新完成时刷新使用
         * @param source  加载目标
         * @param size    图片的显示大小
         */
        public FaceSpan(Context context, final View view, Object source, final int size) {
            super(context, R.drawable.default_face, ALIGN_BOTTOM);//虽然设置了默认的表情，但是并不显示，只是用于占位
            this.mView = view;
            Glide.with(context).load(source).fitCenter().into(new SimpleTarget<GlideDrawable>(size, size) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    mDrawable = resource.getCurrent();
                    int width = mDrawable.getIntrinsicWidth();//获取自测量高宽
                    int height = mDrawable.getIntrinsicHeight();
                    //设置进去
                    mDrawable.setBounds(0, 0, width > 0 ? width : size, height > 0 ? height : size);
                    mView.invalidate();//通知刷新
                }
            });
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Rect rect = mDrawable != null ? mDrawable.getBounds() : new Rect(0, 0, mSize, mSize);//走我们自己的逻辑；进行测量
            if (fm != null) {
                fm.ascent = -rect.bottom;
                fm.descent = 0;
                fm.top = fm.ascent;
                fm.bottom = 0;
            }
            return rect.right;
        }

        @Override
        public Drawable getDrawable() {
            return mDrawable;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            //增加判断
            if (mDrawable != null) {
                super.draw(canvas, text, start, end, x, top, y, bottom, paint);
            }
        }
    }


    /**
     * 拿一个Bean
     *
     * @param context
     * @param key
     * @return
     */
    public static Bean get(Context context, String key) {
        init(context);
        if (FACE_MAP.containsKey(key)) {
            return FACE_MAP.get(key);
        }
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
