package project.com.ningbaoqi.baotalkerclient.fragment.assist;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import project.com.ningbaoqi.baotalkerclient.R;

/**
 * 权限弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());//先使用一个默认的
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_permission, container, false);
        return root;
    }
}
