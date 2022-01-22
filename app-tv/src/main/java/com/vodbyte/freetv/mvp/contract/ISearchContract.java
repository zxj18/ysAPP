package com.vodbyte.freetv.mvp.contract;

import com.vodbyte.freetv.bean.VodBean;

import java.util.List;

public interface ISearchContract {
    interface View{
        void onError(String msg);
        void setSearchResult(List<VodBean> list);
    }

    interface Presenter{
        void Search(String key,int page);
    }
}
