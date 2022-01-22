package com.vodbyte.freetv.app;

import android.os.Environment;

import com.vodbyte.freetv.BuildConfig;

/**
 * 全局配置
 */
public class Constant {

    public static final int VIDEO_HISTORY_NUM = 40;
    public static final String LOG_TAG = "freetv_LOG";
    public static final String TIP_ERROR_GET_DATA = "获取信息出错！";

    public static final String BASE_API_URL = BuildConfig.BASE_API_URL;
    public static final String BASE_API_URL_JSON = BASE_API_URL + "/api_tv/v1/";

    public static final String BASE_API_UPDATE_URL = BASE_API_URL + "/api_tv/update";

    // 直播列表
    public static final String CONFIG = "config";
    public static final String LIVE_LIST = "live_list";
    public static final String FEED_BACK = "feedback";
    public static final String SEARCH = "get_search";
    // 分类标题
    public static final String CLASSIFY_TITLE = "classify_title";
    //分类名称
    public static final String CLASSIFY_LIST = "classify_vod_list";
    //视频详情
    public static final String VOD_ITEM_DETAIL = "vod_item_detail";
    // 首页列表数据
    public static final String HOME_DATA_LIST = "home_data_list";
    public static final String HOME_VOD_ITEM_DETAIL_RELEVANCE_LIST = "vod_item_detail_relevance_list";

    public static final int SETTING_REQUEST_CODE = 200;
    public static final int VIDEO_COLLECTION_NUM = 100;
    public static final int VIDEO_LIST_COLUMN = 5;
    public static final int ANIMATION_DURATION = 300;
    public static final int ANIMATION_ZOOM_IN_DURATION = 200;
    public static final int ANIMATION_ZOOM_OUT_DURATION = 100;
    public static final float ANIMATION_ZOOM_IN_SCALE = 1.15f;
    public static final float ANIMATION_ZOOM_OUT_SCALE = 1.1f;


    public static final boolean FEATURE_2 = true;
    public static final boolean FEATURE_3 = true;
    public static final boolean FEATURE_4 = true;
    public static final boolean FEATURE_10 = true;

}
