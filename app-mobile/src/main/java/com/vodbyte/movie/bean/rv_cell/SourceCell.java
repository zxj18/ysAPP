package com.vodbyte.movie.bean.rv_cell;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;

/**
 * 集数cell
 */
public class SourceCell extends BaseRvCell<VodBean.VodPlayBean> {

    private TextView mTitleView;
    private boolean mSelect = false;

    public SourceCell(VodBean.VodPlayBean vodPlayBean) {
        super(vodPlayBean);
    }

    @Override
    public void releaseResource() {
        mTitleView.setBackgroundResource(R.drawable.bg_btn_vod_cell);
    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_SOURCE_CELL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_source,parent,false);
        return new BaseRvViewHolder(view);
    }

    public void setCellBgColor(boolean isSelect) {
        try {
            if (isSelect) {
                mTitleView.setBackgroundResource(R.drawable.shape_textview_cart);
            } else {
                mTitleView.setBackgroundResource(R.drawable.bg_btn_vod_cell);
            }
        }catch (Exception e) {
            Log.d("setCellBgColor",e.getMessage());
        }
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, final int position) {
        mTitleView = holder.getTextView(R.id.tv_play_num_cell);
        if (mData.getNote().isEmpty()) {
            mTitleView.setText(mData.getNote());
        }else {
            mTitleView.setText(mData.getFrom());
        }

        if (mSelect) {
            setCellBgColor(true);
        }

        if (mListener != null){
            holder.itemView.setOnClickListener(v -> mListener.onClickItem(mData,position));
        }
    }
}
