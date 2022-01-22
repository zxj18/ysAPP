package com.vodbyte.freetv.mvp.model;

import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.mvp.model.dto.ConfigDTO;
import com.vodbyte.freetv.mvp.model.service.ApiService;
import com.vodbyte.freetv.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class ConfigModel {

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
