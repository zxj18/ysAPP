package com.vodbyte.movie.mvp.model;

import android.util.Log;

import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.mvp.model.db.CollectBean;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.db.HistoryBean;
import com.vodbyte.movie.mvp.model.dto.StringDataDTO;
import com.vodbyte.movie.mvp.model.dto.VodDetailDTO;
import com.vodbyte.movie.mvp.model.dto.VodListDTO;
import com.vodbyte.movie.mvp.model.service.ApiService;
import com.vodbyte.movie.utils.RetrofitUtil;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import org.litepal.crud.callback.SaveCallback;

import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class VodDetailModel {

    /**
     * 添加广告日志
     * @param observer
     */
    public void addAdLog(Observer<StringDataDTO> observer, String uuid, String adType, boolean status){
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
    public void getVodRelevanceList(Observer<VodListDTO> observer, final long vodId,final int cid){
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
    public void addHistory(VodBean vodBean){
        HistoryBean historyBean = new HistoryBean();
        historyBean.setvodPrimaryKey(vodBean.getVid());
        historyBean.setTime(new Date());
        historyBean.saveAsync().listen(new SaveCallback() {
            @Override
            public void onFinish(boolean success) {

            }
        });
    }

    /**
     * 目录界面有的章节需要显示的小红点
     * @param list
     * @param firstOpenLastchapterId
     */
    private void setRedPoint(List<ChapterBean> list,long firstOpenLastchapterId){
        for (ChapterBean bean : list) {
            bean.setHasRedPoint(bean.getChapterId() > firstOpenLastchapterId);
        }
    }

}
