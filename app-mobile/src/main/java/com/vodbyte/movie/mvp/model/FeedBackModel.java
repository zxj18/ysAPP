package com.vodbyte.movie.mvp.model;

import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.dto.ConfigDTO;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;
import com.vodbyte.movie.mvp.model.service.ApiService;
import com.vodbyte.movie.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class FeedBackModel {

    /**
     * 获取配置
     * @param observer
     */
    public void sendFeedBack(Observer<StringDataDTO> observer,String vid, String content,String uuid){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .sendFeedBack(vid,content,uuid)
                .map(new Function<StringDataDTO, StringDataDTO>() {
                    @Override
                    public StringDataDTO apply(@NotNull StringDataDTO stringDataDTO) throws Exception {
                        return stringDataDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
