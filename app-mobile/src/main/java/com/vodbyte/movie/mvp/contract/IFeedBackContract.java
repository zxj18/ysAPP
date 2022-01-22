package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.mvp.model.dto.StringDataDTO;

public interface IFeedBackContract {

    interface Presenter{
        void sendFeedBack(String vid,String content,String uuid);
    }

    interface View{
        void onFeedBack(StringDataDTO bean);
        void onError(String msg);
    }
}
