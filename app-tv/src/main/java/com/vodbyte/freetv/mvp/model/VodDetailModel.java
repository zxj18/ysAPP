package com.vodbyte.freetv.mvp.model;


import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.bean.HistoryBean;
import com.vodbyte.freetv.bean.VodBean;
import com.vodbyte.freetv.mvp.model.dto.VodDetailDTO;
import com.vodbyte.freetv.mvp.model.dto.VodListDTO;
import com.vodbyte.freetv.mvp.model.service.ApiService;
import com.vodbyte.freetv.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;
import org.litepal.crud.callback.SaveCallback;

import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class VodDetailModel {

    public void getVodDetailData(Observer<VodDetailDTO> observer, final long vodId){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getVodDetail(vodId)
                .map(new Function<VodDetailDTO, VodDetailDTO>() {
                    @Override
                    public VodDetailDTO apply(@NotNull VodDetailDTO vodDetailDTO) throws Exception {
                        return vodDetailDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取相关视频
     * @param observer
     * @param vodId
     */
    public void getVodRelevanceList(Observer<VodListDTO> observer, final long vodId, final int cid){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON)
                .create(ApiService.class)
                .getVodRelevanceList(vodId,cid)
                .map(new Function<VodListDTO, VodListDTO>() {
                    @Override
                    public VodListDTO apply(@NotNull VodListDTO vodListDTO) throws Exception {
                        return vodListDTO;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 向数据库添加历史记录
     * @param vodBean
     */
    private void addHistory(VodBean vodBean){
        HistoryBean historyBean = new HistoryBean();
        historyBean.setvodPrimaryKey(vodBean.getVid());
        historyBean.setTime(new Date());
        historyBean.saveAsync().listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {

            }
        });
    }
}
