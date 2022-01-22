package com.vodbyte.movie.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;
import com.vodbyte.movie.mvp.model.db.CollectBean;

import org.litepal.LitePal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VodUtil {

    /**
     * 判断是否收藏
     * @param primaryKey
     * @return
     */
    public static boolean isVodCollect(long primaryKey)
    {
        //查询是否被收藏
        int count = LitePal.
                where("vodPrimaryKey = ?",primaryKey+"")
                .count(CollectBean.class);
        return (count > 0);
    }
    /**
     * 收藏书籍
     * @param isCollect
     * @param primaryKey
     */
    public static void vodCollect(boolean isCollect,long primaryKey) {
        if (isCollect){
            //添加收藏
            CollectBean bean = new CollectBean();
            bean.setVodPrimaryKey(primaryKey);
            bean.setTime(new Date());
            bean.save();
        }else {
            LitePal.where("vodPrimaryKey = ?",String.valueOf(primaryKey))
                    .findFirst(CollectBean.class).delete();
        }
    }

    public static void uploadVodEvent(Context mContext, String actType, String vodName, String vid,String url,String vodFrom) {
        Map<String, Object> playInfo = new HashMap<String, Object>();
        playInfo.put("url",url);
        playInfo.put("vod_name",vodName);
        playInfo.put("vod_id",vid);
        playInfo.put("vod_from",vodFrom);
        MobclickAgent.onEventObject(mContext, actType, playInfo);
    }

}
