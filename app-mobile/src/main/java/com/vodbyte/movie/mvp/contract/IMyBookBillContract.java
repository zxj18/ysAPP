package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.mvp.model.vo.MyBookBillVO;

import java.util.List;

public interface IMyBookBillContract {
    interface View{
        void setMyBookBillData(List<MyBookBillVO> oldList, List<MyBookBillVO> newList);
        void onError(String msg);
    }

    interface Presenter{
        void getMyBookBillData();
        void startEdit();
        void endEdit();
        void deleteBooks();
        void selectAll(boolean isSelect);
    }
}
