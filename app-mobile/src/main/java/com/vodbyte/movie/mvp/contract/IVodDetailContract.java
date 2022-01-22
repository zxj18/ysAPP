package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.mvp.model.dto.VodDetailDTO;

import java.util.List;

public interface IVodDetailContract {
    interface View{
        void setAdLog(String data);
        void setVodDetailData(VodDetailDTO vodDetailData);
        void onError(String msg);
    }

    interface Presenter{
        void addAdLog(String uuid,String adType,boolean status);
        void getVodDetailData(long vodId);
        void addHistory(VodBean bean);
    }
}
