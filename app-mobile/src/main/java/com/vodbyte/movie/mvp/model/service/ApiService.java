package com.vodbyte.movie.mvp.model.service;

import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.dto.ClassifyListDTO;
import com.vodbyte.movie.mvp.model.dto.ClassifyTitleDTO;
import com.vodbyte.movie.mvp.model.dto.ConfigDTO;
import com.vodbyte.movie.mvp.model.dto.GirlsDTO;
import com.vodbyte.movie.mvp.model.dto.HomePageDataDTO;
import com.vodbyte.movie.mvp.model.dto.HomeVodBeanListDataDTO;
import com.vodbyte.movie.mvp.model.dto.LiveAreaDTO;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;
import com.vodbyte.movie.mvp.model.dto.TopVodDTO;
import com.vodbyte.movie.mvp.model.dto.VodDetailDTO;
import com.vodbyte.movie.mvp.model.dto.VodListDTO;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET(Constant.CONFIG)
    Observable<ConfigDTO> getConfig();

    @POST(Constant.ADD_AD_LOG)
    Observable<StringDataDTO> addAdLog(@Query("uuid") String uuid,
                                       @Query("ad_type") String adType,
                                       @Query("status") int status);

    @POST(Constant.FEED_BACK)
    Observable<StringDataDTO> sendFeedBack(@Query("vid") String vid,
                                           @Query("content") String content,
                                           @Query("uuid") String uuid);

    @GET(Constant.LIVE_LIST)
    Observable<LiveAreaDTO> getLiveList();

    // 首页列表数据
    @GET(Constant.HOME_DATA_LIST)
    Observable<HomePageDataDTO> getHomePageData(@Query("cid") int cid,
                                                @Query("home_style") int homeStyle);

    //美图
    @GET(Constant.GIRLS_LIST)
    Observable<GirlsDTO> getGirlsList(@Query("page") int page);

    // 首页列表数据
    @GET(Constant.HOME_DATA_LIST)
    Observable<HomeVodBeanListDataDTO> getHomeVodBeanListData(@Query("cid") int cid,
                                                              @Query("home_style") int homeStyle,
                                                              @Query("page") int page);    // 首页列表数据
    @GET(Constant.TOP_VOD_DATA_LIST)
    Observable<TopVodDTO> getTopVodBeanListData(@Query("page") int page);

    // 分类标题
    @GET(Constant.CLASSIFY_TITLE)
    Observable<ClassifyTitleDTO> getClassifyTitle(@Query("is_home") int isHome);

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
