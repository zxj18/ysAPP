package com.vodbyte.freetv.mvp.contract;
import com.vodbyte.freetv.bean.LiveAreaBean;

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
