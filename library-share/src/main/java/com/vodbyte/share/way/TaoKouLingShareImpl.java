package com.vodbyte.share.way;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.vodbyte.share.info.IShareInfo;

/**
 * 淘口令复制
 */
public class TaoKouLingShareImpl extends BaseShareWay {

    public TaoKouLingShareImpl(Activity activity, int resIcon, String title) {
        super(activity, resIcon, title);
    }

    @Override
    public void onSystemShare(IShareInfo shareInfo) {

    }

    @Override
    public void onShare(IShareInfo shareInfo) {
        try {
            //获取剪贴板管理器：
            ClipboardManager clipboardManager = (ClipboardManager) mActivity.getSystemService(mActivity.CLIPBOARD_SERVICE);

            String text = String.format("%s \r\n%s",shareInfo.getShareContent(),shareInfo.getShareUrl());
            ClipData mClipData = ClipData.newPlainText("Label", text);
            // 将ClipData内容放到系统剪贴板里。
            clipboardManager.setPrimaryClip(mClipData);
            Toast.makeText(mActivity,"复制成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
