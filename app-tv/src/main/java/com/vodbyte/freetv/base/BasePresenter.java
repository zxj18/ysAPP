package com.vodbyte.freetv.base;


public class BasePresenter<V>{

    public final String TAG = "FreeTv";

    private V view;

    public void attachView(V v){
        view = v;
    }

    public void detachView(){
        view = null;
    }

    public boolean isAttachView(){
        return view != null;
    }

    public V getMVPView(){
        return view;
    }

}
