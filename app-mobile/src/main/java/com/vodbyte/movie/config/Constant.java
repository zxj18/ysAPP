package com.vodbyte.movie.config;

import com.vodbyte.movie.BuildConfig;

public class Constant {

    public static final String TIP_ERROR_GET_DATA = "获取信息出错！";

    public static final int TYPE_RANK = 0;//排行
    public static final int TYPE_BOOK_BILL = 1;//书单

    public static final int KIND_POPULARITY_LIST = 0;//人气榜
    public static final int KIND_REWARD_LIST = 1;//打赏榜
    public static final int KIND_MONTHLY_TICKET_LIST = 2;//月票榜

    public static final String TIP_NOT_COMPLETE = "这个功能还没有实现！";

    //Web端JSON Base
    public static final String BASE_API_URL = BuildConfig.BASE_API_URL;
    public static final String BASE_API_URL_JSON = BASE_API_URL + "/api/v1/";
    public static final String BASE_API_UPDATE_URL = BASE_API_URL + "/api/update";
    public static String PIC_DOMAIN_URL = "";

    // 直播列表
    public static final String CONFIG = "config";
    public static final String LIVE_LIST = "live_list";
    public static final String FEED_BACK = "feedback";
    public static final String ADD_AD_LOG = "add_ad_log";
    // 分类标题
    public static final String CLASSIFY_TITLE = "classify_title";
    //分类名称
    public static final String CLASSIFY_LIST = "classify_vod_list";
    public static final String GIRLS_LIST = "girls_list";
    //视频详情
    public static final String VOD_ITEM_DETAIL = "vod_item_detail";
    // 首页列表数据
    public static final String HOME_DATA_LIST = "home_data_list";
    public static final String TOP_VOD_DATA_LIST = "top_vod_data_list";
    public static final String HOME_VOD_ITEM_DETAIL_RELEVANCE_LIST = "vod_item_detail_relevance_list";

    //搜索/获取分类地址
    public static final String SEARCH = "get_search";
    public static final String HOT_KEYS = "get_hot_search";

}
