package com.vodbyte.movie.mvp.model;

import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.dto.GirlsDTO;
import com.vodbyte.movie.mvp.model.dto.LiveAreaDTO;
import com.vodbyte.movie.mvp.model.service.ApiService;
import com.vodbyte.movie.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 *  电视直播
 */
public class GirlsModel {
    /**
     * 获取直播地区列表
     * @param observer
     */
    public void getGirlsList(Observer<GirlsDTO> observer,int page){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getGirlsList(page)
                .map(new Function<GirlsDTO, GirlsDTO>() {
                    @Override
                    public GirlsDTO apply(@NotNull GirlsDTO girlsDTO) throws Exception {
                        return girlsDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
