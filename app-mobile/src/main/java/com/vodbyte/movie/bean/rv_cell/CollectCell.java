package com.vodbyte.movie.bean.rv_cell;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.base.BaseBookShelfCell;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.mvp.model.vo.CollectVO;

public class CollectCell extends BaseBookShelfCell<CollectVO> {
    public CollectCell(CollectVO collectVO) {
        super(collectVO);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_SUBSCRIBE;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return createSimpleHolder(parent, R.layout.cell_book_collect);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        TextView tvIsUpdated = holder.getTextView(R.id.tv_is_update_cell_book_collect);
        TextView tvName = holder.getTextView(R.id.tv_name_cell_book_collect);
        ImageView ivCover = holder.getImageView(R.id.iv_cover_cell_book_collect);

        if (mData.getVodBean().getVod_serial().equals("0") || mData.getVodBean().getVod_serial().equals("")) {
            tvIsUpdated.setText(mData.getVodBean().getVod_remarks());
        }else {
            tvIsUpdated.setText(mData.getVodBean().getVod_serial());
        }

        tvName.setText(mData.getVodBean().getVod_name());
        Glide.with(holder.getContext()).load(mData.getVodBean().getVod_pic()).into(ivCover);
    }
}
