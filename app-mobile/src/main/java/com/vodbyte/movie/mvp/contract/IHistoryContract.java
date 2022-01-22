package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.mvp.model.vo.HistoryVO;

import java.util.List;

public interface IHistoryContract {
    interface View{
        void setHistoryData(List<HistoryVO> oldList, List<HistoryVO> newList);
        void onError(String msg);
        void noMore();
    }

    interface Presenter{
        void refresh();
        void getHistoryData();
        void startEdit();
        void endEdit();
        void deleteBooks();
        void selectAll(boolean isSelect);
    }
}
