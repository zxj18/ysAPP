package com.vodbyte.freetv.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vodbyte.freetv.bean.LiveAreaBean;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.mvp.view.activity.detail.VideoDetailActivity;
import com.vodbyte.freetv.mvp.view.activity.home.HomeActivity;
import com.vodbyte.freetv.mvp.view.activity.live.IjkTVLiveActivity;
import com.vodbyte.freetv.mvp.view.activity.detail.FullScreenPlayActivity;

public class StartActUtil {
    public static void toMainAct(Context context){
        context.startActivity(new Intent(context, HomeActivity.class));
        ((Activity)context).finish();
    }

    /**
     * 跳转详情界面
     * @param context
     */
    public static void toPlayDetail(Context context, VodBean vodBean){
        Intent intent = new Intent(context, VideoDetailActivity.class);
        intent.putExtra("vod",vodBean.toJsonString());
        context.startActivity(intent);
    }

    /**
     * 跳转播放界面
     * @param context
     */
    public static void toPlayPlayActivity(Context context, String title,String url){
        Intent intent = new Intent(context, FullScreenPlayActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("url",url);
        context.startActivity(intent);
    }

    /**
     * 跳转直播界面
     * @param context
     */
    public static void toLivePlayDetail(Context context, LiveAreaBean.LiveBean liveBean){
        Intent intent = new Intent(context, IjkTVLiveActivity.class);
        intent.putExtra("live",liveBean.toJsonString());
        context.startActivity(intent);
    }
}
