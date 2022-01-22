package com.vodbyte.freetv.mvp.model.service;


import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.mvp.model.dto.ClassifyListDTO;
import com.vodbyte.freetv.mvp.model.dto.ClassifyTitleDTO;
import com.vodbyte.freetv.mvp.model.dto.ConfigDTO;
import com.vodbyte.freetv.mvp.model.dto.HomePageDataDTO;
import com.vodbyte.freetv.mvp.model.dto.HomeVodBeanListDataDTO;
import com.vodbyte.freetv.mvp.model.dto.LiveAreaDTO;
import com.vodbyte.freetv.mvp.model.dto.SearchDTO;
import com.vodbyte.freetv.mvp.model.dto.StringDataDTO;
import com.vodbyte.freetv.mvp.model.dto.VodDetailDTO;
import com.vodbyte.freetv.mvp.model.dto.VodListDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET(Constant.CONFIG)
    Observable<ConfigDTO> getConfig();

    @POST(Constant.FEED_BACK)
    Observable<StringDataDTO> sendFeedBack(@Query("vid") String vid,
                                           @Query("content") String content,
                                           @Query("uuid") String uuid);

    @GET(Constant.LIVE_LIST)
    Observable<LiveAreaDTO> getLiveList();

    @GET(Constant.SEARCH)
    Observable<SearchDTO> search(@Query("word") String word,
                                 @Query("page") int page);

    // 首页列表数据
    @GET(Constant.HOME_DATA_LIST)
    Observable<HomePageDataDTO> getHomePageData(@Query("cid") int cid,
                                                @Query("home_style") int homeStyle);

    // 首页列表数据
    @GET(Constant.HOME_DATA_LIST)
    Observable<HomeVodBeanListDataDTO> getHomeVodBeanListData(@Query("cid") int cid,
                                                              @Query("home_style") int homeStyle,
                                                              @Query("page") int page);

    // 分类标题
    @GET(Constant.CLASSIFY_TITLE)
    Observable<ClassifyTitleDTO> getClassifyTitle(@Query("is_tv") boolean isTv);

    // 分类列表
    @GET(Constant.CLASSIFY_LIST)
    Observable<ClassifyListDTO> getClassifyList(@Query("page") int page,
                                                @Query("cid") int cid,
                                                @Query("vod_class") String className,
                                                @Query("area") String area,
                                                @Query("lang") String lang,
                                                @Query("year") int year);


    //  视频详情
    @GET(Constant.VOD_ITEM_DETAIL)
    Observable<VodDetailDTO> getVodDetail(@Query("vid") long vid);

    @GET(Constant.HOME_VOD_ITEM_DETAIL_RELEVANCE_LIST)
    Observable<VodListDTO> getVodRelevanceList(@Query("vid") long vid,
                                               @Query("cid") long cid);

}
