package com.vodbyte.share;

import android.app.Activity;

import com.vodbyte.share.way.BaseShareWay;
import com.vodbyte.share.way.ImagesShareImpl;
import com.vodbyte.share.way.SHARE_MEDIA;
import com.vodbyte.share.way.SaveShareImpl;
import com.vodbyte.share.way.TaoKouLingShareImpl;
import com.vodbyte.share.way.QQShareImpl;
import com.vodbyte.share.way.SinaShareImpl;
import com.vodbyte.share.way.WeChatShareImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShareManager {

    private static ShareSetting setting;

    static {
        setting = ShareSetting.getInstance();
    }

    public static ShareSetting init() {
        return setting;
    }

    public static List<BaseShareWay> getShareWay(Activity activity) {
        List<BaseShareWay> list = new ArrayList<>();
        //如果不设置分享的icon 则使用默认样式的分享图标
        if (getShareWayIconMap().isEmpty()) {
            for (SHARE_MEDIA item : ShareSetting.getInstance().getShareMedia()) {
                switch (item) {
                    case WEICHAT:
                        list.add(new WeChatShareImpl(activity, R.drawable.ic_share_dialog_weixin, activity.getString(R.string.title_share_weixin), WeChatShareImpl.SHARE_WEICHAT));
                        break;
                    case WEICHATCIRCLE:
                        list.add(new WeChatShareImpl(activity, R.drawable.ic_share_dialog_weixin_quan, activity.getString(R.string.title_share_momment), WeChatShareImpl.SHARE_MOMMENT));
                        break;
                    case SINA:
                        list.add(new SinaShareImpl(activity, R.drawable.ic_share_dialog_weibo_sina, activity.getString(R.string.title_share_sina)));
                        break;
                    case QQ:
                        list.add(new QQShareImpl(activity, R.drawable.ic_share_dialog_qq, activity.getString(R.string.title_share_qq), QQShareImpl.SHARE_QQ));
                        break;
                    case QQZONE:
                        list.add(new QQShareImpl(activity, R.drawable.ic_share_dialog_qzone, activity.getString(R.string.title_share_qzeon), QQShareImpl.SHARE_Qzeon));
                        break;
                    case COPYLINK:
                        list.add(new TaoKouLingShareImpl(activity, R.drawable.ic_share_dialog_copy_link, activity.getString(R.string.title_share_copy_link)));
                        break;
                    case DOWNLOAD:
                        list.add(new SaveShareImpl(activity,R.drawable.ic_share_dialog_image, activity.getString(R.string.title_share_save)));
                        break;
                    case IMAGES:
                        list.add(new ImagesShareImpl(activity,R.drawable.ic_share_dialog_down, activity.getString(R.string.title_share_image)));
                        break;
                    default:
                        break;
                }
            }
        } else {//自定义了图标
            LinkedHashMap<SHARE_MEDIA, Integer> iconMap = getShareWayIconMap();
            for (SHARE_MEDIA item : ShareSetting.getInstance().getShareMedia()) {
                switch (item) {
                    case WEICHAT:
                        list.add(new WeChatShareImpl(activity, iconMap.get(SHARE_MEDIA.WEICHAT), activity.getString(R.string.title_share_weixin), WeChatShareImpl.SHARE_WEICHAT));
                        break;
                    case WEICHATCIRCLE:
                        list.add(new WeChatShareImpl(activity, iconMap.get(SHARE_MEDIA.WEICHATCIRCLE), activity.getString(R.string.title_share_momment), WeChatShareImpl.SHARE_MOMMENT));
                        break;
                    case SINA:
                        list.add(new SinaShareImpl(activity, iconMap.get(SHARE_MEDIA.SINA), activity.getString(R.string.title_share_sina)));
                        break;
                    case QQ:
                        list.add(new QQShareImpl(activity, iconMap.get(SHARE_MEDIA.QQ), activity.getString(R.string.title_share_qq), QQShareImpl.SHARE_QQ));
                        break;
                    case QQZONE:
                        list.add(new QQShareImpl(activity, iconMap.get(SHARE_MEDIA.QQZONE), activity.getString(R.string.title_share_qzeon), QQShareImpl.SHARE_Qzeon));
                        break;
                    case COPYLINK:
                        list.add(new TaoKouLingShareImpl(activity, iconMap.get(SHARE_MEDIA.COPYLINK), activity.getString(R.string.title_share_copy_link)));
                        break;
                    case DOWNLOAD:
                        list.add(new SaveShareImpl(activity, iconMap.get(SHARE_MEDIA.DOWNLOAD), activity.getString(R.string.title_share_save)));
                        break;
                    case IMAGES:
                        list.add(new ImagesShareImpl(activity, iconMap.get(SHARE_MEDIA.IMAGES), activity.getString(R.string.title_share_image)));
                        break;
                    default:
                        break;
                }
            }
        }

        return list;
    }

    public static String getQQAppId() {
        return setting.getQQAppId();
    }

    public static String getWechatAppId() {
        return setting.getWechatAppId();
    }

    public static String getWeiboAppId() {
        return setting.getWeiboAppId();
    }

    public static String getAppName() {
        return setting.getAppName();
    }

    public static String getDefShareImageUrl() {
        return setting.getDefShareImageUrl();
    }

    public static int getDefShareImageUrlId() {
        return setting.getDefShareImageUrlId();
    }

    public static String getSinaRedirectUrl() {
        return setting.getSinaRedirectUrl();
    }

    public static String getScope() {
        return setting.getScope();
    }

    public static LinkedHashMap<SHARE_MEDIA, Integer> getShareWayIconMap() {
        return setting.getShareWayIconMap();
    }
}
