package project.com.ningbaoqi.baotalkerclient.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.dashen.ningbaoqi.factory.presenter.group.GroupCreateContract;
import com.dashen.ningbaoqi.factory.presenter.group.GroupCreatePresenter;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.CheckBox;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.baotalkerclient.fragment.media.GalleryFragment;
import project.com.ningbaoqi.common.app.Application;
import project.com.ningbaoqi.common.app.PresenterToolbarActivity;
import project.com.ningbaoqi.common.widget.a.PortraitView;
import project.com.ningbaoqi.common.widget.recycler.RecyclerAdapter;

public class GroupCreateActivity extends PresenterToolbarActivity<GroupCreateContract.Presenter> implements GroupCreateContract.View {
    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.edit_name)
    EditText mName;
    @BindView(R.id.edit_desc)
    EditText mDesc;
    @BindView(R.id.im_portrait)
    PortraitView mPortrait;
    private RecyclerAdapter<GroupCreateContract.ViewModel> mAdapter;
    private String mPortraitPath;//头像的地址

    public static void show(Context context) {
        context.startActivity(new Intent(context, GroupCreateActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(mAdapter = new Adapter());
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        hideSoftKeyboard();
        new GalleryFragment().setListener(new GalleryFragment.OnSelectedListener() {
            @Override
            public void onSelectedImage(String path) {
                UCrop.Options options = new UCrop.Options();
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);//设置图片处理的格式JPEG
                options.setCompressionQuality(96);//设置图片压缩后的精度
                File file = Application.getPorttraitTmpFile();//本地头像缓存地址
                UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(file))
                        .withAspectRatio(1, 1)//保证正方形所以采用1：1
                        .withMaxResultSize(520, 520)//最大的大小时520*520像素
                        .withOptions(options).start(GroupCreateActivity.this);//根据配置剪切
            }
        }).show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }

    /**
     * 收到从Activity中传递过来的回调，然后取出其中的数据，进行图片加载
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
        }
    }

    /**
     * 将截取的图片放在指定的控件中并且上传到OSS服务器上
     *
     * @param uri
     */
    private void loadPortrait(Uri uri) {
        mPortraitPath = uri.getPath();//得到头像地址
        Glide.with(this).load(uri).asBitmap().centerCrop().into(mPortrait);//将剪切得到的Uri设置到头像
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            onCreateClick();//进行创建
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 进行创建操作
     */
    private void onCreateClick() {
        hideSoftKeyboard();
        String name = mName.getText().toString().trim();
        String desc = mDesc.getText().toString().trim();
        mPresenter.create(name, desc, mPortraitPath);
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyboard() {
        View view = getCurrentFocus();//当前焦点的一个View
        if (view == null) {
            return;
        }
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateSucceed() {

    }

    @Override
    protected GroupCreateContract.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    @Override
    public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {

    }

    private class Adapter extends RecyclerAdapter<GroupCreateContract.ViewModel> {
        @Override
        protected int getItemViewType(int position, GroupCreateContract.ViewModel viewModel) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View root, int viewType) {
            return new GroupCreateActivity.ViewHolder(root);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCreateContract.ViewModel> {
        @BindView(R.id.im_portrait)
        PortraitView mPortrait;
        @BindView(R.id.txt_name)
        TextView mName;
        @BindView(R.id.cb_select)
        CheckBox mSelect;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCreateContract.ViewModel viewModel) {
            mPortrait.setup(Glide.with(GroupCreateActivity.this), viewModel.author);
            mName.setText(viewModel.author.getName());
            mSelect.setChecked(viewModel.isSelected);
        }

        @OnCheckedChanged(R.id.cb_select)
        void onCheckedChanged(boolean checked) {//更改选中状态
            mPresenter.changeSelect(mData, checked);
        }
    }
}
