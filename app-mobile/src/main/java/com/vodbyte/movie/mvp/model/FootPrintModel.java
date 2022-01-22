package com.vodbyte.movie.mvp.model;

import android.util.Log;

import com.vodbyte.movie.bean.VodBean;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FootPrintModel {

    public void getMyCollect(Observer<List<VodBean>> observer){
        Observable.create(new ObservableOnSubscribe<List<VodBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<VodBean>> emitter){
                try {
                    List<VodBean> list = LitePal.where("isCollect = ?","1").find(VodBean.class);
                    Log.d("FootPrintModel", "subscribe: "+list.size());
                    emitter.onNext(list);
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(e);
                }finally {
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getHistoryRead(Observer<List<VodBean>> observer){
        Observable.create(new ObservableOnSubscribe<List<VodBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<VodBean>> emitter){
                try {
                    emitter.onNext(LitePal.where("").find(VodBean.class));
                }catch (Exception e){
                    e.printStackTrace();
                    emitter.onError(e);
                }finally {
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
