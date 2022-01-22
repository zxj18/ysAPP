package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.BookBillBean;
import com.vodbyte.movie.bean.LiveAreaBean;

import java.util.List;

public interface ILiveAreaContract {
    interface view{
        void setLiveAreaList(List<LiveAreaBean> list);
        void showError(String msg);
    }

    interface presenter{
        void getLiveAreaList();
    }
}
