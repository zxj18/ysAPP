package com.vodbyte.share.way;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.vodbyte.share.R;
import com.vodbyte.share.ShareManager;
import com.vodbyte.share.ShareUtils;
import com.vodbyte.share.Util;
import com.vodbyte.share.info.IShareInfo;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * Created by GuDong on 8/20/15.
 */
public class WeChatShareImpl extends BaseShareWay {
    public static final int SHARE_WEICHAT = 1;
    public static final int SHARE_MOMMENT = 2;
    private int mShareWay = SHARE_WEICHAT;

    private static final int THUMB_SIZE = 150;

    private IWXAPI mWeixinAPI;
    public WeChatShareImpl(Activity activity, int resIcon, String title, int shareWay) {
        super(activity, resIcon, title);
        this.mShareWay = shareWay;
        mWeixinAPI = WXAPIFactory.createWXAPI(mActivity, ShareManager.getWechatAppId());
    }

    @Override
    public void onSystemShare(IShareInfo shareInfo) {
        ArrayList<String> images = new ArrayList<>();
        images.add(shareInfo.getShareImgUrl());
        switch (mShareWay) {
            case SHARE_WEICHAT:
                ShareUtils.shareMoreImageToApp(mActivity,images,shareInfo.getShareContent(),ShareUtils.SHARE_TYPE.SHARE_TYPE_WECHAT);
                break;
            case SHARE_MOMMENT:
                ShareUtils.shareMoreImageToApp(mActivity,images,shareInfo.getShareContent(),ShareUtils.SHARE_TYPE.SHARE_TYPE_TIME_LINE);
                break;
        }
    }

    @Override
    public void onShare(IShareInfo shareInfo) {
        if(!mWeixinAPI.isWXAppInstalled()){
            Toast.makeText(mActivity, mActivity.getString(R.string.share_weixin_not_install), Toast.LENGTH_SHORT).show();
            return;
        }
        if(mWeixinAPI.getWXAppSupportAPI() < Build.PAY_SUPPORTED_SDK_INT){
            Toast.makeText(mActivity, mActivity.getString(R.string.share_weixin_not_support), Toast.LENGTH_SHORT).show();
            return;
        }
        shareToWeixin(shareInfo.getShareTitle(),shareInfo.getShareContent(),shareInfo.getShareImgUrl(),mShareWay==SHARE_MOMMENT, shareInfo.getShareUrl());
    }

    @SuppressLint("StaticFieldLeak")
    private void shareToWeixin(final String title, final String content, String imgUrl, final boolean friend, final String shareUrl) {
        new AsyncTask<String, Void, Bitmap>() {
            WXMediaMessage msg;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                WXWebpageObject webpage = new WXWebpageObject();
                msg = new WXMediaMessage(webpage);
                msg.mediaObject = webpage;
                webpage.webpageUrl = shareUrl;
                msg.title = title;
                msg.description = limitString(content, 1024);
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                return  getBitmapThumb(strings[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                msg.thumbData = Util.bmpToByteArray(bitmap, true);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = friend ? SendMessageToWX.Req.WXSceneTimeline
                        : SendMessageToWX.Req.WXSceneSession;
                mWeixinAPI.sendReq(req);
            }
        }.execute(imgUrl);
    }

    private Bitmap getBitmapThumb(final String shareImageUrl) {
        Bitmap thumb = null;
        if (TextUtils.isEmpty(shareImageUrl)) {
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), ShareManager.getDefShareImageUrlId());
            thumb = compressImage(thumb, 32);
        } else {
            try {
                Bitmap bmp = BitmapFactory.decodeStream(new URL(shareImageUrl).openStream());
                thumb = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
            }catch (Exception e){
                e.printStackTrace();
                thumb = null;
            }
        }
        if (thumb == null)
            thumb = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.ic_launcher);
        return thumb;
    }
    private String limitString(String result, int byteLength) {

        byte[] bytes = result.getBytes();
        while (bytes.length >= byteLength) {
            String lString = result.substring(0, result.length() - 2);
            bytes = lString.getBytes();
        }
        return new String(bytes);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private static Bitmap compressImage(Bitmap image, int limit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length > limit * 1024) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

}
