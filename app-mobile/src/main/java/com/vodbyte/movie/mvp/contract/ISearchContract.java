package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.SearchHistoryBean;
import com.vodbyte.movie.bean.HotKeyBean;

import java.util.List;

public interface ISearchContract {
    interface View{
        void setHotKeys(List<HotKeyBean> list);
        void setHistoryList(List<SearchHistoryBean> list);
        void onError(String msg);
    }

    interface Presenter{
        void getHotKeys();
        void getHistoryList();
        void addHistory(List<SearchHistoryBean> list, SearchHistoryBean searchHistoryBean);
        void saveHistoryList(List<SearchHistoryBean> list);
    }
}
