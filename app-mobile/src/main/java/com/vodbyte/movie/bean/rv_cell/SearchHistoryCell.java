package com.vodbyte.movie.bean.rv_cell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.SearchHistoryBean;
import com.vodbyte.movie.config.ItemType;

public class SearchHistoryCell extends BaseRvCell<SearchHistoryBean> {

    public SearchHistoryCell(SearchHistoryBean searchHistoryBean) {
        super(searchHistoryBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_SEARCH_HISTORY;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRvViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_search_history,parent,false));
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        TextView tvHistory = holder.getTextView(R.id.tv_history_cell_search_history);
        ImageView ivDelete = holder.getImageView(R.id.iv_delete_cell_search_history);
        tvHistory.setText(mData.getKey());
        if (mListener != null){
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v,position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClickItem(mData,position);
                }
            });
        }
    }
}
