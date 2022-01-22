package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.ClassifyTitleBean;

import java.util.List;

public interface IHomeContract {

    interface Presenter{
        void getCidTitleList(boolean isHome);
    }

    interface View{
        void setCidTitle(List<ClassifyTitleBean> list);
        void onError(String msg);
    }
}
