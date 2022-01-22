package com.vodbyte.freetv.mvp.contract;
import com.vodbyte.freetv.mvp.model.dto.VodDetailDTO;

import java.util.List;

public interface IVodDetailContract {
    interface View{
        void setVodDetailData(VodDetailDTO vodDetailData);
        void onError(String msg);
    }

    interface Presenter{
        void getVodDetailData(long vodId);
        void collect(boolean isCollect);
    }
}
