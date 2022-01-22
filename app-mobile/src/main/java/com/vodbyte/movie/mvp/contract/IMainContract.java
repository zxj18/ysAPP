package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.ClassifyTitleBean;

import java.util.List;

public interface IMainContract {

    interface Presenter{
        void getCidTitleList(boolean isHome);
    }

    interface View{
        void setCidTitle(List<ClassifyTitleBean> list);
    }
}
