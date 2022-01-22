package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.mvp.model.vo.CollectVO;

import java.util.List;

public interface ICollectContract {
    interface View{
        void setCollectData(List<CollectVO> oldList, List<CollectVO> newList);
        void onError(String msg);
        void noMore();
    }

    interface Presenter{
        void refresh();
        void getCollectData();
        void startEdit();
        void endEdit();
        void deleteBooks();
        void selectAll(boolean isSelect);
    }
}
