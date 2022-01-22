package com.vodbyte.movie.mvp.contract;

import com.vodbyte.movie.mvp.model.vo.BookBillDetailVO;

import java.util.ArrayList;

public interface IBookBillDetailContract {
    interface View{
        void setBookDetailData(BookBillDetailVO data);
        void onError(String msg);
    }

    interface Presenter {
        void getBookBillDetailData(String bookId);
        void collectBookBill(boolean isCollect);
        void updateBookBill();
        void setCoverBookIdList(ArrayList<String> coverBookIdList);
    }
}
