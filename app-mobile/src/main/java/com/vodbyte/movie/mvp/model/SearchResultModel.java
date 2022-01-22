package com.vodbyte.movie.mvp.model;

import com.vodbyte.movie.config.Constant;
import com.vodbyte.movie.mvp.model.dto.SearchDTO;
import com.vodbyte.movie.mvp.model.service.SearchService;
import com.vodbyte.movie.utils.RetrofitUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchResultModel {

    public void searchWord(Observer<SearchDTO> observer, String word, int page){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON).create(SearchService.class)
                .search(word,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
