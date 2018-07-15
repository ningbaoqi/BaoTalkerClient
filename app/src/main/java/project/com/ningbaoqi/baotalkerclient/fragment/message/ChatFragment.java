package project.com.ningbaoqi.baotalkerclient.fragment.message;

import android.os.Bundle;

import project.com.ningbaoqi.baotalkerclient.activities.MessageActivity;
import project.com.ningbaoqi.common.app.Fragment;

public abstract class ChatFragment extends Fragment {
    private String mReceiverId;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(MessageActivity.KEY_RECEIVER_ID);
    }

    
}
