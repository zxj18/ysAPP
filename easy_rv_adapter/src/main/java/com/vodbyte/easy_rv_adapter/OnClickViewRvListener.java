package com.vodbyte.easy_rv_adapter;

import android.view.View;

import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;

public interface OnClickViewRvListener {
    void onClick(View view,int position);
    <C> void onClickItem(C data,int position);
}