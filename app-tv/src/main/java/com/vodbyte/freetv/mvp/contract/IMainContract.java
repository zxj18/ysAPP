package com.vodbyte.freetv.mvp.contract;

import com.vodbyte.freetv.bean.ClassifyTitleBean;

import java.util.List;

public interface IMainContract {

    interface Presenter{
        void getCidTitleList();
    }

    interface View{
        void setCidTitle(List<ClassifyTitleBean> list);
    }
}
