package project.com.ningbaoqi.common.widget.recycler;
/**
 * @author ningbaoqi
 */

public interface AdapterCallback<Data> {
    void update(Data data , RecyclerAdapter.ViewHolder<Data> holder);
}
