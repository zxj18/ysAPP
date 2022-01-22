package com.vodbyte.easy_rv_adapter.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.vodbyte.easy_rv_adapter.OnClickViewRvListener;
import com.vodbyte.easy_rv_adapter.OnLoadMoreRvListener;

public abstract class BaseRvCell<T> implements Cell {

    public T mData;

    public OnClickViewRvListener mListener;
    public OnLoadMoreRvListener mLoadMoreRvListener;

    public final BaseRvViewHolder createSimpleHolder(ViewGroup parent,int layoutResId){
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(layoutResId,parent,false));
    }

    @Override
    public long getItemId(int position) {
        return RecyclerView.NO_ID;
    }

    /**
     * 添加监听事件
     * @param mListener
     */
    public void setListener(OnClickViewRvListener mListener) {
        this.mListener = mListener;
    }

    public void setLoadMoreListener(OnLoadMoreRvListener mListener) {
        this.mLoadMoreRvListener = mListener;
    }

    public BaseRvCell(T t){
        mData = t;
    }

}
