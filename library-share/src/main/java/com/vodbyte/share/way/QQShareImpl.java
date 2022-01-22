package com.vodbyte.share.way;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.vodbyte.share.R;
import com.vodbyte.share.ShareManager;
import com.vodbyte.share.ShareUtils;
import com.vodbyte.share.info.IShareInfo;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

/**
 *

 */
public class QQShareImpl extends BaseShareWay {
    public static final int SHARE_QQ = 1;
    public static final int SHARE_Qzeon = 2;
    private int mShareWay = SHARE_QQ;

    private Tencent mTencent;

    public QQShareImpl(Activity activity, int resIcon, String title,int qqShareWay) {
        super(activity, resIcon, title);
        this.mShareWay = qqShareWay;
        mTencent = Tencent.createInstance(ShareManager.getQQAppId(), activity);
    }

    @Override
    public void onSystemShare(IShareInfo shareInfo) {
        ArrayList<String> images = new ArrayList<>();
        images.add(shareInfo.getShareImgUrl());

        switch (mShareWay) {
            case SHARE_QQ:
                ShareUtils.shareMoreImageToApp(mActivity,images,shareInfo.getShareContent(),ShareUtils.SHARE_TYPE.SHARE_TYPE_QQ);
                break;
            case SHARE_Qzeon:
                ShareUtils.shareMoreImageToApp(mActivity,images,shareInfo.getShareContent(),ShareUtils.SHARE_TYPE.SHARE_TYPE_QZONE);
                break;
        }
    }

    @Override
    public void onShare(IShareInfo mInfo) {
        switch (mShareWay) {
            case SHARE_QQ:
                shareToQQ(mInfo.getShareTitle(), mInfo.getShareContent(), mInfo.getShareUrl(), mInfo.getShareImgUrl());
                break;
            case SHARE_Qzeon:
                shareToQzeon(mInfo.getShareTitle(), mInfo.getShareContent(), mInfo.getShareUrl(), mInfo.getShareImgUrl());
                break;
        }
    }

    public void shareToQQ(String title, String content, String shareUrl,
                          String imgUrl) {

        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title); //分享标题
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareUrl); //分享url
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content); //
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, ShareManager.getAppName()); //分享app 名称
        if (!TextUtils.isEmpty(imgUrl)) {
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl); //分享图片
        } else {
            if(!TextUtils.isEmpty( ShareManager.getDefShareImageUrl())){
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, ShareManager.getDefShareImageUrl());
            }
        }

        mTencent.shareToQQ(mActivity, params, new IUiListener() {
            @Override
            public void onError(UiError arg0) {
                String message = mActivity.getString(R.string.share_fail) + " " + arg0.errorMessage;
                Toast.makeText(mActivity, message , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Object arg0) {
                Toast.makeText(mActivity, mActivity.getString(R.string.share_success) , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(mActivity,  mActivity.getString(R.string.share_cancel) , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWarning(int i) {

            }
        });
    }

    public void shareToQzeon(String title, String content, String shareUrl,
                             String imgUrl) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, shareUrl);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);
        params.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, ShareManager.getAppName());
        ArrayList<String> list = new ArrayList<String>();
        if (!TextUtils.isEmpty(imgUrl)) {
            list.add(imgUrl);
        } else {
            if(!TextUtils.isEmpty(ShareManager.getDefShareImageUrl())){
                list.add(ShareManager.getDefShareImageUrl());
            }
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        mTencent.shareToQzone(mActivity, params, new IUiListener() {

            @Override
            public void onCancel() {
                Toast.makeText(mActivity,  mActivity.getString(R.string.share_cancel) , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWarning(int i) {

            }

            @Override
            public void onError(UiError e) {
                String message = mActivity.getString(R.string.share_fail) + " " + e.errorMessage;
                Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(Object response) {
                Toast.makeText(mActivity, mActivity.getString(R.string.share_success) , Toast.LENGTH_SHORT).show();
            }

        });
    }
}
