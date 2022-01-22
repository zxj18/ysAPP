package com.vodbyte.share.way;

import android.app.Activity;

import com.vodbyte.share.info.IShareInfo;

/**
 * 图片邀请
 */
public class ImagesShareImpl extends BaseShareWay {

    public ImagesShareImpl(Activity activity, int resIcon, String title) {
        super(activity, resIcon, title);
    }

    @Override
    public void onSystemShare(IShareInfo shareInfo) {

    }

    @Override
    public void onShare(IShareInfo shareInfo) {

    }
}
