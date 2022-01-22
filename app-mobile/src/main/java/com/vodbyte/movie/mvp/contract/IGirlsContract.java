package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.bean.GirlBean;
import com.vodbyte.movie.bean.LiveAreaBean;

import java.util.List;

public interface IGirlsContract {
    interface view{
        void setGirlsList(List<GirlBean> list);
        void showError(String msg);
    }

    interface presenter{
        void getGirlsList(int page);
    }
}
