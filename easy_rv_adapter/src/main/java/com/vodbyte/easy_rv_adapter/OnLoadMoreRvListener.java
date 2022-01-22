package com.vodbyte.easy_rv_adapter;

import android.view.View;

import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;

public interface OnLoadMoreRvListener {
    void onLoadMore(View view,int position);
}