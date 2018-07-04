package project.com.ningbaoqi.baotalkerclient.fragment.assist;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.com.ningbaoqi.baotalkerclient.R;
import project.com.ningbaoqi.common.app.Application;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 权限弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {
    private static final int RC = 0X0100;//权限回调的标识

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());//先使用一个默认的
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_permission, container, false);
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {//找到按钮并添加单击事件，点击时申请权限
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());//界面显示的时候进行刷新
    }

    /**
     * 刷新我们的布局中的图片的状态
     *
     * @param root
     */
    private void refreshState(View root) {
        if (root == null) {
            return;
        }
        Context context = getContext();
        root.findViewById(R.id.im_state_permission_network).setVisibility(haveNetwork(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_read).setVisibility(haveReadPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_write).setVisibility(haveWritePerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.im_state_permission_record_audio).setVisibility(haveRecordPerm(context) ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否有网络权限
     *
     * @param context
     * @return
     */
    private static boolean haveNetwork(Context context) {
        String[] permissions = new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE};//需要检查的网络权限
        return EasyPermissions.hasPermissions(context, permissions);
    }

    /**
     * 是否有外部存储读取权限
     *
     * @param context
     * @return
     */
    private static boolean haveReadPerm(Context context) {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        return EasyPermissions.hasPermissions(context, permissions);
    }

    /**
     * 是否有外部存储写入权限
     *
     * @param context
     * @return
     */
    private static boolean haveWritePerm(Context context) {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        return EasyPermissions.hasPermissions(context, permissions);
    }

    /**
     * 是否有录音权限
     *
     * @param context
     * @return
     */
    private static boolean haveRecordPerm(Context context) {
        String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
        return EasyPermissions.hasPermissions(context, permissions);
    }


    /**
     * 私有的show方法，用于自己调度
     *
     * @param manager
     */
    private static void show(FragmentManager manager) {
        new PermissionsFragment().show(manager, PermissionsFragment.class.getName());
    }


    /**
     * 检查是否具有所有的权限
     *
     * @param context context
     * @param manager manager
     * @return 是否有权限
     */
    public static boolean haveAll(Context context, FragmentManager manager) {
        boolean haveAll = haveNetwork(context) && haveReadPerm(context) && haveWritePerm(context) && haveRecordPerm(context);//检查是否具有所有的权限
        if (!haveAll) {//如果没有所有的权限则显示当前权限申请的界面
            show(manager);
        }
        return haveAll;
    }

    /**
     * 申请权限的方法
     */
    @AfterPermissionGranted(RC)//获取权限之后还会回调
    private void requestPermission() {
        String[] permissions = new String[]{Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        if (EasyPermissions.hasPermissions(getContext(), permissions)) {
            Application.showToast(R.string.label_permission_ok);
            refreshState(getView());//在Fragment中可以直接调用getView得到根布局，必须在onCreateView方法之后
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions), RC, permissions);
        }
    }

    /**
     * 当权限申请成功回调
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    /**
     * 当权限申请失败回调
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {//如果有没有申请成功的权限存在，则弹出弹出框，用户点击后去设置界面自己打开权限
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    /**
     * 权限申请的时候回调的方法，在这个方法中把对应的权限申请状态交给EasyPermissions框架
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);//传递对应的参数，并且告知接收权限的处理者是我自己
    }
}
