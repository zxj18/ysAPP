package com.vodbyte.movie.utils.diffutil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import com.vodbyte.movie.mvp.model.vo.MyBookBillVO;

import java.util.List;

public class BookBillDiffUtil extends DiffUtil.Callback {
    private List<MyBookBillVO> mOldDatas,mNewDatas;

    public BookBillDiffUtil(List<MyBookBillVO> mOldDatas, List<MyBookBillVO> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    @Override
    public int getOldListSize() {
        return mOldDatas == null?0:mOldDatas.size();
    }

    @Override
    public int getNewListSize() {
        return mNewDatas==null?0:mNewDatas.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewDatas.get(newItemPosition).getBookBillBean().getBookBillId()
                .equals(mOldDatas.get(oldItemPosition).getBookBillBean().getBookBillId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        MyBookBillVO oldItem = mOldDatas.get(oldItemPosition);
        MyBookBillVO newItem = mNewDatas.get(newItemPosition);
        return oldItem.isSelect() == newItem.isSelect()
                && oldItem.isStartSelect() == newItem.isStartSelect()
                && oldItem.getBookBillBean().getNum() == newItem.getBookBillBean().getNum()
                && oldItem.getBookBillBean().getTitle().equals(newItem.getBookBillBean().getTitle())
                && newItem.getBookBillBean().getBookIdList().containsAll(oldItem.getBookBillBean().getBookIdList());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
