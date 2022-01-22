package com.vodbyte.share.way;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.Toast;

import com.vodbyte.share.info.IShareInfo;

/**
 * 保存图片
 */
public class SaveShareImpl extends BaseShareWay {

    public SaveShareImpl(Activity activity, int resIcon, String title) {
        super(activity, resIcon, title);
    }

    @Override
    public void onSystemShare(IShareInfo shareInfo) {

    }

    @Override
    public void onShare(IShareInfo shareInfo) {

    }
}
