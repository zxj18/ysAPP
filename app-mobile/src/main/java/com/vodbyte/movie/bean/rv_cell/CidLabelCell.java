package com.vodbyte.movie.bean.rv_cell;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.config.ItemType;

import java.util.List;

import master.flame.danmaku.danmaku.loader.ILoader;

public class CidLabelCell extends BaseRvCell<String> {

    private View mItemView;
    public CidLabelCell(String title) {
        super(title);
    }

    @Override
    public void releaseResource() {
        mItemView.setBackgroundColor(Color.parseColor("#00000000"));
    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_CID_LABEL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createSimpleHolder(parent,R.layout.cell_cid_label);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        TextView tvCidLabel = holder.getTextView(R.id.tv_cid_label);
        tvCidLabel.setText(mData);

        if (position == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#2C7BF1"));
        }
        mItemView = holder.itemView;

        if (mListener != null){
            holder.itemView.setOnClickListener(v -> {
                mListener.onClickItem(mData,position);
                mListener.onClick(holder.itemView,position);
            });
        }
    }
}
