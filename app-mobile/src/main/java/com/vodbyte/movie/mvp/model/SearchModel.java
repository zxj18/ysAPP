package com.vodbyte.movie.mvp.model;

import com.vodbyte.movie.bean.SearchHistoryBean;
import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.dto.HotKeyDTO;
import com.vodbyte.movie.mvp.model.service.SearchService;
import com.vodbyte.movie.utils.RetrofitUtil;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchModel {
    public void getHotKeys(Observer<HotKeyDTO> observer){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON).create(SearchService.class)
                .getHotKeys()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getSearchHistory(Observer<List<SearchHistoryBean>> observer){
        Observable.create(new ObservableOnSubscribe<List<SearchHistoryBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SearchHistoryBean>> emitter) {
                List<SearchHistoryBean> list = LitePal.findAll(SearchHistoryBean.class);
                emitter.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void saveHistoryList(final List<SearchHistoryBean> list){
        LitePal.deleteAll(SearchHistoryBean.class);
        LitePal.saveAll(list);
    }
}
