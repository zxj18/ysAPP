package com.vodbyte.movie.mvp.model;

import com.vodbyte.movie.bean.RankBean;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.dto.ClassifyListDTO;
import com.vodbyte.movie.mvp.model.dto.ClassifyTitleDTO;
import com.vodbyte.movie.mvp.model.dto.ConfigDTO;
import com.vodbyte.movie.mvp.model.dto.HomePageDataDTO;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;
import com.vodbyte.movie.mvp.model.service.ApiService;
import com.vodbyte.movie.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class ConfigModel {

    /**
     * 添加广告日志
     * @param observer
     */
    public void addAdLog(Observer<StringDataDTO> observer,String uuid,String adType,boolean status){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .addAdLog(uuid,adType,status ? 1 : 0)
                .map(new Function<StringDataDTO, StringDataDTO>() {
                    @Override
                    public StringDataDTO apply(@NotNull StringDataDTO dataDTO) throws Exception {
                        return dataDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取配置
     * @param observer
     */
    public void getConfig(Observer<ConfigDTO> observer){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getConfig()
                .map(new Function<ConfigDTO, ConfigDTO>() {
                    @Override
                    public ConfigDTO apply(@NotNull ConfigDTO configDTO) throws Exception {
                        return configDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
