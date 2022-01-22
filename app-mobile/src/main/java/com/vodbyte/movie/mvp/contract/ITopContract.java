package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.RankBean;
import com.vodbyte.movie.bean.VodBean;

import java.util.List;

public interface ITopContract {
    interface View{
        void setTopData(List<VodBean> list);
        void onError(String msg);
    }

    interface Presenter{
        void getTopData(int page);
    }
}
