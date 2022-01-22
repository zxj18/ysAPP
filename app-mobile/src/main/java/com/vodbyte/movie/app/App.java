package com.vodbyte.movie.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import org.litepal.LitePal;

import com.cdnbye.sdk.P2pEngine;
import com.fiio.sdk.FiioSDK;
import com.lxj.xpopup.XPopup;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.vodbyte.movie.BuildConfig;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.utils.DeviceIDUtils;
import com.vodbyte.share.ShareManager;
import com.vodbyte.share.way.SHARE_MEDIA;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.player.VideoViewConfig;
import com.vodbyte.videoplayer.player.VideoViewManager;
import com.vodbyte.videoplayer.ijk.IjkPlayerFactory;
import com.tencent.bugly.crashreport.CrashReport;
import java.util.LinkedHashMap;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        LitePal.getDatabase();

        mContext = getApplicationContext();

        //广告SDK
        try {

            UMConfigure.preInit(this, BuildConfig.UMENG_APPKEY,"");
            UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,"");
            UMConfigure.setProcessEvent(true);
            MobclickAgent.onProfileSignIn(DeviceIDUtils.getDeviceId(this));

            ConfigBean configBean = ConfigBean.getConfig(this);
            if (configBean != null) {

                //P2P
                if(!configBean.getP2p_token().isEmpty()){
                    P2pEngine.init(this,configBean.getP2p_token().trim(), null);
                }

                if (configBean.getAd().isOpen_ad()) {
                  //  Log.d("adinfo",configBean.getAd().toString());
                    FiioSDK.initSdk(getApplicationContext(), configBean.getAd().getAd_appid(), configBean.getAd().getAd_appkey());
                }
            }
        }catch (Exception e) {

        }

        XPopup.setPrimaryColor(getResources().getColor(R.color.colorPrimary));

        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
        CrashReport.initCrashReport(this, "56b56fb60c", false);

        //播放器配置，注意：此为全局配置，按需开启
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                .setLogEnabled(BuildConfig.DEBUG)
                .setPlayerFactory(IjkPlayerFactory.create())
                .setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT).build());


        //分享配置
        {
            LinkedHashMap<SHARE_MEDIA, Integer> iconMap = new LinkedHashMap<>();

            iconMap.put(SHARE_MEDIA.COPYLINK, R.drawable.ic_share_dialog_copy_link);
            iconMap.put(SHARE_MEDIA.WEICHAT, R.drawable.ic_share_dialog_weixin);
            iconMap.put(SHARE_MEDIA.WEICHATCIRCLE, R.drawable.ic_share_dialog_weixin_quan);
            iconMap.put(SHARE_MEDIA.SINA, R.drawable.ic_share_dialog_weibo_sina);
            iconMap.put(SHARE_MEDIA.QQ, R.drawable.ic_share_dialog_qq);
            iconMap.put(SHARE_MEDIA.QQZONE, R.drawable.ic_share_dialog_qzone);
            iconMap.put(SHARE_MEDIA.DOWNLOAD, R.drawable.ic_share_dialog_down);
            iconMap.put(SHARE_MEDIA.IMAGES, R.drawable.ic_share_dialog_image);

            ShareManager.init()
                    //应用的名字
                    .setAppName(getResources().getString(R.string.app_name))
                    .setDefShareImageUrl("http://b.hiphotos.baidu.com/image/h%3D200/sign=9a3972dc65d9f2d33f1123ef99ed8a53/3b87e950352ac65cf1f52b4efcf2b21193138a1f.jpg")
                    .addShareMedia(SHARE_MEDIA.QQZONE, SHARE_MEDIA.QQ, SHARE_MEDIA.SINA, SHARE_MEDIA.WEICHATCIRCLE, SHARE_MEDIA.WEICHAT,SHARE_MEDIA.COPYLINK)
                    .setShareWayIconMap(iconMap)
                    .setQQAppId(AppConst.QQ_APPID)
                    .setWeiboAppId(AppConst.QEIBO_APPID)
                    .setWechatAppId(AppConst.WeCahtAPPID)
                    .setSinaRedirectUrl("https://api.weibo.com/oauth2/default.html")
                    .setScope("email,direct_messages_read,direct_messages_write,\"\n" +
                            "            + \"friendships_groups_read,friendships_groups_write,statuses_to_me_read,\"\n" +
                            "            + \"follow_app_official_microblog,\" + \"invitation_write")
                    .setDefImageUrlId(R.drawable.ic_launcher);
        }

    }

    public static Context getAppContext() {
        return mContext;
    }

    public static Resources getAppResources() {
        return mContext.getResources();
    }

}
