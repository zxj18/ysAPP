package com.vodbyte.freetv.mvp.model;


import com.vodbyte.freetv.app.Constant;
import com.vodbyte.freetv.mvp.model.dto.SearchDTO;
import com.vodbyte.freetv.mvp.model.service.ApiService;
import com.vodbyte.freetv.utils.RetrofitUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchModel {

    public void searchKey(Observer<SearchDTO> observer, int page, String word){
        RetrofitUtil.bind(Constant.BASE_API_URL_JSON).create(ApiService.class)
                .search(word,page)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
