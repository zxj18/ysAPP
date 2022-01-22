package com.vodbyte.movie.bean.rv_cell;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.ChapterBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.utils.ScreenUtil;

/**
 * 集数cell
 */
public class SubSetNumCell extends BaseRvCell<VodBean.VodPlayBean.UrlBean> {

    private TextView mTitleView;
    private boolean mSelect = false;

    public SubSetNumCell(VodBean.VodPlayBean.UrlBean urlBean) {
        super(urlBean);
    }

    @Override
    public void releaseResource() {
        mTitleView.setBackgroundResource(R.drawable.bg_btn_vod_cell);
    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_CELL_SUBSET_NUM;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_subset_num,parent,false);
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
        mTitleView.setText(mData.getName());
        if (mSelect) {
            setCellBgColor(true);
        }
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
