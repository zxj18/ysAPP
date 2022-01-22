package com.vodbyte.movie.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.vodbyte.movie.bean.LiveAreaBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.kkcling.DlnaPopup;
import com.vodbyte.movie.mvp.view.activity.FeedBackActivity;
import com.vodbyte.movie.mvp.view.activity.LaunchActivity;
import com.vodbyte.movie.mvp.view.activity.LivePlayActivity;
import com.vodbyte.movie.mvp.view.activity.MainActivity;
import com.vodbyte.movie.mvp.view.activity.PlayDetailActivity;
import com.vodbyte.movie.mvp.view.activity.SearchActivity;
import com.vodbyte.movie.mvp.view.activity.SearchResultActivity;
import com.vodbyte.movie.mvp.view.fragment.BookShelf.VodShelfActivity;
import com.vodbyte.movie.widget.ListDrawerPopupView;

import java.util.ArrayList;

/**
 * 对startActivity的封装
 */
public class StartActUtil {

    /**
     * 跳转播放界面
     * @param context
     */
    public static void toPlayDetail(Context context, VodBean vodBean){
        Intent intent = new Intent(context, PlayDetailActivity.class);
        intent.putExtra("vod",vodBean.toJsonString());
        context.startActivity(intent);
    }

    /**
     * 跳转直播界面
     * @param context
     */
    public static void toLivePlayDetail(Context context, LiveAreaBean.LiveBean liveBean){
        Intent intent = new Intent(context, LivePlayActivity.class);
        intent.putExtra("live",liveBean.toJsonString());
        context.startActivity(intent);
    }

    public static void toSearchAct(Context context){
        context.startActivity(new Intent(context,SearchActivity.class));
    }

    public static void toMainAct(Context context){
        context.startActivity(new Intent(context,MainActivity.class));
        ((Activity)context).finish();
    }

    public static void toDlnaView(Context context,String title,String url){
        new XPopup.Builder(context)
                .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                .enableDrag(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                .asCustom(new DlnaPopup(context,title,url)/*.enableDrag(false)*/)
                .show();
    }

    public static void toFeedBack(Context context,String vid, String content){
        Intent intent = new Intent(context, FeedBackActivity.class);
        intent.putExtra("vid",vid);
        intent.putExtra("content",content);
        context.startActivity(intent);
    }
    public static void toVodShelfActivity(Context context){
        Intent intent = new Intent(context, VodShelfActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转到搜索结果
     * @param context
     * @param title 搜索标题
     * @param word 搜索key
     */
    public static void toSearchResultAct(Context context, String title, String word){
        Intent intent = new Intent(context,SearchResultActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("word",word);
        context.startActivity(intent);
    }
}
