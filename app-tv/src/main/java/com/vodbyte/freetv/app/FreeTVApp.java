package com.vodbyte.freetv.app;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.vodbyte.freetv.BuildConfig;
import com.vodbyte.freetv.utils.AppTools;
import com.vodbyte.videoplayer.ijk.IjkPlayerFactory;
import com.vodbyte.videoplayer.player.VideoView;
import com.vodbyte.videoplayer.player.VideoViewConfig;
import com.vodbyte.videoplayer.player.VideoViewManager;

import org.litepal.LitePal;

public class FreeTVApp extends Application {

    public static int screenWidth = 0;
    public static int screenHeight = 0;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 数据库
        LitePal.initialize(this);
        LitePal.getDatabase();
        AppTools.init(this);

        //播放器配置，注意：此为全局配置，按需开启
        VideoViewManager.setConfig(VideoViewConfig.newBuilder()
                .setLogEnabled(false)
                .setPlayerFactory(IjkPlayerFactory.create())
                .setScreenScaleType(VideoView.SCREEN_SCALE_DEFAULT)
                .build());

        try {
            CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG);
            CrashReport.initCrashReport(this, "56b56fb60c", false);
            UMConfigure.preInit(this, BuildConfig.UMENG_APPKEY,"");
            UMConfigure.init(this, UMConfigure.DEVICE_TYPE_BOX,"");
            UMConfigure.setProcessEvent(true);
        } catch (Exception e) {

        }

    }
}
