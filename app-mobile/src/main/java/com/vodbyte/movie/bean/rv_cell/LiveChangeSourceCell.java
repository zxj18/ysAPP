package com.vodbyte.movie.bean.rv_cell;

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
import com.vodbyte.movie.bean.LiveAreaBean;
import com.vodbyte.movie.config.ItemType;

public class LiveChangeSourceCell extends BaseRvCell<LiveAreaBean.LiveBean.LiveItemBean> {

    public LiveChangeSourceCell(LiveAreaBean.LiveBean.LiveItemBean liveItemBean) {
        super(liveItemBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_LIVE_SOURCE;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createSimpleHolder(parent,R.layout.cell_live_change_source);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        TextView tvTitle = holder.getTextView(R.id.tv_title_cell);
        tvTitle.setText(mData.getTitle());
        if (mListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(mData,position);
                }
            });
        }
    }
}
