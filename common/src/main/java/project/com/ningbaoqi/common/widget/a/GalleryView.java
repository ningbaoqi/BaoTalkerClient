package project.com.ningbaoqi.common.widget.a;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import project.com.ningbaoqi.common.R;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;

/**
 * TODO: document your custom view class.
 */
public class GalleryView extends RecyclerView {
    private Adapter mAdapter = new Adapter();
    private LoaderCallback loaderCallback = new LoaderCallback();
    private static final int LOADER_ID = 0X0100;
    private static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;//最小的图片大小
    private List<Image> mSelectedImages = new LinkedList<>();//因为不需要遍历，会随时的添加和删除，所以使用该List性能更好
    private static final int MAX_IMAGE_COUNT = 3;//最大的选中图片的数量
    private SelectedChangedListener selectedChangedListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));//设置布局管理器，网格布局，一行四列
        setAdapter(mAdapter);
        mAdapter.setListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, Image image) {
                //Cell点击操作，如果点击是允许的，就更新对应的cell的状态，然后更新界面，如果不能允许点击(已经达到了最大的选中数量) 那么就不刷新界面
                if (onItemSelectClick(image)) {
                    holder.updateData(image);
                }
            }
        });
    }

    /**
     * 得到选中的图片的全部地址
     *
     * @return 地址数组
     */
    public String[] getSelectImagePath() {
        String[] paths = new String[mSelectedImages.size()];
        int index = 0;
        for (Image ima : mSelectedImages) {
            paths[index++] = ima.path;
        }
        return paths;
    }

    /**
     * 可以清空选中的图片
     */
    public void clear() {
        for (Image ima : mSelectedImages) {
            ima.isSelect = false;//一定要先重置状态
        }
        mSelectedImages.clear();
        mAdapter.notifyDataSetChanged();//通知更新
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        SelectedChangedListener mListener = selectedChangedListener;//得到监听者并判断是否有监听者，然后进行回调数量变化
        if (mListener != null) {
            mListener.onSelectedCountChanged(mSelectedImages.size());
        }
    }

    /**
     * 初始化方法
     *
     * @param manager Loader管理器
     * @return 返回Loader id用于销毁Loader
     */
    public int setUp(LoaderManager manager, SelectedChangedListener listener) {
        manager.initLoader(LOADER_ID, null, loaderCallback);//初始化Loader
        Log.d("nbq", "listener" + listener);
        selectedChangedListener = listener;
        return LOADER_ID;
    }

    /**
     * 用于实际的数据加载的Loader
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        private final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,//ID
                MediaStore.Images.Media.DATA,//图片路径
                MediaStore.Images.Media.DATE_ADDED//图片的创建时间
        };

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {//创建Loader
            Log.d("nbq", IMAGE_PROJECTION[0] + "--" + IMAGE_PROJECTION[1] + "-- " + IMAGE_PROJECTION[2] + "--");
            if (id == LOADER_ID) {//如果是我们的ID则可以进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC");//倒序查询
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {//当Loader加载完成回调
            List<Image> images = new ArrayList<>();
            if (data != null) {//判断是否有数据
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();//移动游标到顶部
                    int indexID = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);//得到对应的列的index坐标，不要再循环中出现，而是要
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexdate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        int id = data.getInt(indexID);
                        String path = data.getString(indexPath);
                        long date = data.getLong(indexdate);
                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) {
                            continue;//如果没有图片或者图片大小太小则继续
                        }
                        Image image = new Image();
                        image.id = id;
                        image.path = path;
                        image.date = date;
                        Log.d("nbq", id + "--" + path + "-- " + date + "--");
                        images.add(image);//添加一条新的数据
                    } while (data.moveToNext());
                }
            }
            updateSource(images);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {//当Loader销毁或者重置回调，进行界面清空操作
            updateSource(null);
        }
    }

    /**
     * 通知Adapter数据更改的方法
     *
     * @param images 新的数据
     */
    private void updateSource(List<Image> images) {
        mAdapter.replace(images);
    }


    /**
     * Cell点击的具体逻辑
     *
     * @return true进行数据更改，需要刷新；false不刷新
     */
    private boolean onItemSelectClick(Image image) {
        //是否进行刷新
        boolean notifyRefresh;
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);//如果之前在现在就移除
            image.isSelect = false;
            notifyRefresh = true;//状态已经改变，则需要更新
        } else {
            if (mSelectedImages.size() >= MAX_IMAGE_COUNT) {
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, MAX_IMAGE_COUNT);
                //Toast一个提示
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                mSelectedImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        if (notifyRefresh) {//如果数据有更改，那么我们需要通知外面的监听者我们的数据选中改变了
            notifySelectChanged();
        }
        return true;
    }


    /**
     * 内部的数据结构bean
     */
    private static class Image {
        int id;//数据的ID
        String path;//数据的路径
        long date;//图片的创建日期
        boolean isSelect;//图片是否选中

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Image image = (Image) obj;
            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }
    }

    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_gallery;//已经规定了设置的是布局ID
        }

        @Override
        protected ViewHolder onCreateViewHolder(View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }
    }

    /**
     * cell对应的holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {
        private ImageView mPic;
        private View mShader;
        private CheckBox mSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mPic = itemView.findViewById(R.id.im_image);
            mShader = itemView.findViewById(R.id.view_shade);
            mSelected = itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(Image image) {//进行数据绑定
            Glide.with(getContext()).load(image.path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存，直接从原图加载路径
                    .centerCrop().placeholder(R.color.grey_200)//默认颜色，如果有些加载耗时
                    .into(mPic);
            mShader.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mSelected.setChecked(image.isSelect);
            mSelected.setVisibility(VISIBLE);
        }
    }

    /**
     * 对外的一个监听器
     */
    public interface SelectedChangedListener {
        void onSelectedCountChanged(int count);
    }
}
