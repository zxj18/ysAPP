package com.vodbyte.share.way;

import android.app.Activity;
import com.vodbyte.share.ShareUtils;
import com.vodbyte.share.info.IShareInfo;
import java.util.ArrayList;

public class SinaShareImpl extends BaseShareWay {

    public SinaShareImpl(Activity activity, int resIcon, String title) {
        super(activity, resIcon, title);
    }

    @Override
    public void onSystemShare(IShareInfo shareInfo) {
        ArrayList<String> images = new ArrayList<>();
        images.add(shareInfo.getShareImgUrl());
        ShareUtils.shareMoreImageToApp(mActivity, images, shareInfo.getShareContent(), ShareUtils.SHARE_TYPE.SHARE_TYPE_WEI_BO);
    }

    @Override
    public void onShare(IShareInfo shareInfo) {
        ArrayList<String> images = new ArrayList<>();
        images.add(shareInfo.getShareImgUrl());
        ShareUtils.shareMoreImageToApp(mActivity, images, shareInfo.getShareContent(), ShareUtils.SHARE_TYPE.SHARE_TYPE_WEI_BO);
    }


}
