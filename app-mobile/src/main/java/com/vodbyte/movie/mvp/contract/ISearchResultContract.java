package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.VodBean;

import java.util.List;

public interface ISearchResultContract {
    interface View{
        void onError(String msg);
        void noMore();
        void setSearchResult(List<VodBean> list);
    }

    interface Presenter{
        void SearchWord(String word,int page);
    }
}
