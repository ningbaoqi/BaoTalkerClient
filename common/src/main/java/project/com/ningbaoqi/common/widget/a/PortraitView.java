package project.com.ningbaoqi.common.widget.a;


import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;

import de.hdodenhof.circleimageview.CircleImageView;
import project.com.ningbaoqi.common.R;
import project.com.ningbaoqi.factory.model.Author;

/**
 * 头像View
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null) {
            url = "";
        }
        manager.load(url).placeholder(resourceId).centerCrop().dontAnimate().into(this);
    }

    public void setup(RequestManager manager, String url) {
        this.setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, Author author) {
        if (author == null) {
            return;
        }
        this.setup(manager, author.getPortrait());
    }
}
