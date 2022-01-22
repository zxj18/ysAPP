package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.ClassifyTitleBean;
import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.mvp.model.dto.ConfigDTO;

import java.util.List;

public interface IConfigContract {

    interface Presenter{
        void getConfig();
        void addAdLog(String uuid,String adType,boolean status);
    }

    interface View{
        void setConfig(ConfigBean config);
        void setAdLog(String data);
        void onError(String msg);
    }
}
