package com.vodbyte.freetv.mvp.model;


import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.mvp.model.dto.LiveAreaDTO;
import com.vodbyte.freetv.mvp.model.service.ApiService;
import com.vodbyte.freetv.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 *  电视直播
 */
public class LiveAreaModel {
    /**
     * 获取直播地区列表
     * @param observer
     */
    public void getLiveAreaList(Observer<LiveAreaDTO> observer){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getLiveList()
                .map(new Function<LiveAreaDTO, LiveAreaDTO>() {
                    @Override
                    public LiveAreaDTO apply(@NotNull LiveAreaDTO liveListDTO) throws Exception {
                        return liveListDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
