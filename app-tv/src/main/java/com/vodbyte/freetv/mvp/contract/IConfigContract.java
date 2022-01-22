package com.vodbyte.freetv.mvp.contract;

import com.vodbyte.freetv.bean.ConfigBean;

public interface IConfigContract {

    interface Presenter{
        void getConfig();
    }

    interface View{
        void setConfig(ConfigBean config);
        void onError(String msg);
    }
}
