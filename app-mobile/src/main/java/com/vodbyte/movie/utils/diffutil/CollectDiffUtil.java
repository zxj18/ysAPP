package com.vodbyte.movie.utils.diffutil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.vodbyte.movie.mvp.model.vo.CollectVO;

import java.util.List;

public class CollectDiffUtil extends DiffUtil.Callback {
    private List<CollectVO> mOldDatas,mNewDatas;

    public CollectDiffUtil(List<CollectVO> mOldDatas, List<CollectVO> mNewDatas) {
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
        return mNewDatas.get(newItemPosition).getVodBean().getVid() == mOldDatas.get(oldItemPosition).getVodBean().getVid();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CollectVO newItem = mNewDatas.get(newItemPosition);
        CollectVO oldItem = mOldDatas.get(oldItemPosition);
        return oldItem.isStartSelect() == newItem.isStartSelect()
                && oldItem.isSelect() == newItem.isSelect();
//                && newItem.getBookBean().getLastRecordChapterId() == (oldItem.getBookBean().getLastRecordChapterId())
//                && newItem.getBookBean().isUpdate() == oldItem.getBookBean().isUpdate()
//                && newItem.getBookBean().getRecentChapter().equals((oldItem.getBookBean().getRecentChapter()));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
