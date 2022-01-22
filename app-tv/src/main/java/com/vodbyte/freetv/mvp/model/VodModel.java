package com.vodbyte.freetv.mvp.model;

import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.mvp.model.dto.ClassifyListDTO;
import com.vodbyte.freetv.mvp.model.dto.ClassifyTitleDTO;
import com.vodbyte.freetv.mvp.model.dto.HomePageDataDTO;
import com.vodbyte.freetv.mvp.model.dto.HomeVodBeanListDataDTO;
import com.vodbyte.freetv.mvp.model.service.ApiService;
import com.vodbyte.freetv.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class VodModel {

    /**
     * 获取分类数据
     * @param observer
     * @param page  分页
     * @param cid 分类id
     * @param className 分类名称
     * @param area 地区
     * @param lang 语言
     * @param year 年代
     */
    public void getClassifyBeanList(final Observer<ClassifyListDTO> observer, int page, int cid, String className, String area, String lang, int year){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getClassifyList(page,cid, className, area, lang, year)
                .map(new Function<ClassifyListDTO, ClassifyListDTO>() {
                    @Override
                    public ClassifyListDTO apply(@NotNull ClassifyListDTO classifyListDTO) throws Exception {
                        return classifyListDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getClassifyBeanList2(final Observer<ClassifyListDTO> observer, int page, int cid){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getClassifyList(page,cid,"all","all","all",0)
                .map(new Function<ClassifyListDTO, ClassifyListDTO>() {
                    @Override
                    public ClassifyListDTO apply(@NotNull ClassifyListDTO classifyListDTO) throws Exception {
                        return classifyListDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 分类名称
     * @param observer
     */
    public void getKindTitleList(Observer<ClassifyTitleDTO> observer){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getClassifyTitle(true)
                .map(new Function<ClassifyTitleDTO, ClassifyTitleDTO>() {
                    @Override
                    public ClassifyTitleDTO apply(@NotNull ClassifyTitleDTO classifyListDTO) throws Exception {
                        return classifyListDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取首页数据
     * @param observer
     */
    public void getHomePageData(final Observer<HomePageDataDTO> observer, final int cid, int homeStyle){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getHomePageData(cid,homeStyle)
                .map(new Function<HomePageDataDTO, HomePageDataDTO>() {
                    @Override
                    public HomePageDataDTO apply(@NotNull HomePageDataDTO homePageDataDTO) throws Exception {
                        return homePageDataDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取首页瀑布流数据
     * @param observer
     * @param cid
     * @param page
     */
    public void getHomeVodBeanListData(final Observer<HomeVodBeanListDataDTO> observer, final int cid, int page){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getHomeVodBeanListData(cid,1,page)
                .map(new Function<HomeVodBeanListDataDTO, HomeVodBeanListDataDTO>() {
                    @Override
                    public HomeVodBeanListDataDTO apply(@NotNull HomeVodBeanListDataDTO homeVodBeanListDataDTO) throws Exception {
                        return homeVodBeanListDataDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
