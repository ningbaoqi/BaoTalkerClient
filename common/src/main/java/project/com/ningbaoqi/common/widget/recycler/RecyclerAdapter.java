package project.com.ningbaoqi.common.widget.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import project.com.ningbaoqi.common.R;


public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>> implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {

    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mListener = listener;
        this.mDataList = dataList;
    }

    /**
     * 覆写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，其实覆写后返回的都是XML文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     当前的数据
     * @return XML文件的id 用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型,约定为xml布局的ID
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**
         * 得到LayoutInflater用于把xml初始化为View
         */
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = inflater.inflate(viewType, parent, false);
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);//通过子类必须实现的方法，得到一个ViewHolder
        root.setTag(R.id.tag_recycle_holder, holder);//设置View的tag为ViewHolder，进行双向绑定
        /**
         * 设置事件点击
         */
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);

        holder.unbinder = ButterKnife.bind(holder, root);//进行界面注解绑定
        holder.callback = this;//绑定callback

        return holder;
    }

    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 布局的类型，其实就是XML的ID
     * @return 返回一个ViewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 绑定数据到一个Holder上，通知数据更新的时候，将会回调该方法
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);//得到需要绑定的数据
        holder.bind(data);//触发holder的绑定的方法
    }

    /**
     * 得到当前集合的size
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    /**
     * 返回整个集合
     *
     * @return
     */
    public List<Data> getItems() {
        return mDataList;
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data
     */
    public void add(Data data) {
        mDataList.add(data);
        //notifyDataSetChanged();//通知刷新,但是更新的是所有，其实只需要更新新插入的即可
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据，并通知插入
     *
     * @param dataList
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据，并通知插入
     *
     * @param datas
     */
    public void add(Collection<Data> datas) {
        if (datas != null && datas.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(datas);
            notifyItemRangeInserted(startPos, datas.size());
        }
    }

    /**
     * 清空数据
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * @param data
     * @param holder
     */
    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        int position = holder.getAdapterPosition();//获取目前holder的坐标
        if (position >= 0) {//如果坐标存在 ， 进行数据的移除和更新
            mDataList.remove(position);
            mDataList.add(position, data);
            notifyItemChanged(position);//通知刷新
        }
    }

    /**
     * 单击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycle_holder);
        if (this.mListener != null) {
            int pos = viewHolder.getAdapterPosition();//得到ViewHolder当前对应的适配器的坐标
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }


    /**
     * 长按事件
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycle_holder);
        if (this.mListener != null) {
            int pos = viewHolder.getAdapterPosition();//得到ViewHolder当前对应的适配器的坐标
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    public void setListener(AdapterListener<Data> listener) {
        this.mListener = listener;
    }

    /**
     * 自定义监听器
     *
     * @param <Data>
     */
    public interface AdapterListener<Data> {
        void onItemClick(RecyclerAdapter.ViewHolder holder, Data data);//当cell点击的时候触发

        void onItemLongClick(RecyclerAdapter.ViewHolder holder, Data data);//当cell长按时候触发
    }

    /**
     * 对回调接口做一次实现
     *
     * @param <Data>
     */
    public abstract static class AdapterListenerImpl<Data> implements AdapterListener<Data> {

        @Override
        public void onItemClick(ViewHolder holder, Data data) {

        }

        @Override
        public void onItemLongClick(ViewHolder holder, Data data) {

        }
    }

    /**
     * 自定义的ViewHolder
     *
     * @param <Data> 泛型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        protected Data mData;
        private AdapterCallback<Data> callback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候回调，必须覆写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

        /**
         * holder自己对自己对应的Data进行更新操作
         *
         * @param data data数据
         */
        public void updateData(Data data) {
            if (this.callback != null) {
                this.callback.update(data, this);
            }
        }
    }
}
