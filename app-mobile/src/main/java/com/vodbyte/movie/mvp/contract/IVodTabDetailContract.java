package com.vodbyte.movie.mvp.contract;
import com.vodbyte.movie.mvp.model.dto.VodListDTO;

public interface IVodTabDetailContract {
    interface View{
        void setVodTabData(VodListDTO vodListDTO);
        void onError(String msg);
    }

    interface Presenter{
        void getVodTabData(long vodId,int cid);
    }
}
