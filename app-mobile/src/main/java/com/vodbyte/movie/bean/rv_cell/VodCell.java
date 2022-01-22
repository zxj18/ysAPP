package com.vodbyte.movie.bean.rv_cell;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.BuildConfig;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.utils.ScreenUtil;

public class VodCell extends BaseRvCell<VodBean> {

    private boolean isGrid = false;

    public VodCell(VodBean vodBean) {
        super(vodBean);
    }

    @Override
    public void releaseResource() {

    }

    public void setGrid(boolean isGrid){
        this.isGrid = isGrid;
    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_CELL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vod_item,parent,false);
        if (isGrid){
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            int width = ScreenUtil.getScreenWidth(parent.getContext()) / 3;
            layoutParams.height = width + (width / 2);
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.bottomMargin = ScreenUtil.dip2px(parent.getContext(),2);
            view.setLayoutParams(layoutParams);
        }
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {

        try {

            TextView tvName = holder.getTextView(R.id.tv_name_cell_vod_home);
            // 评分
            TextView tvScore = holder.getTextView(R.id.tv_score_cell_vod_home);
            TextView tvRecentWords = holder.getTextView(R.id.tv_recentWords_cell_vod_home);
            TextView tvSummary = holder.getTextView(R.id.tv_summary_cell_vod_home);
            ImageView ivCover = holder.getImageView(R.id.iv_cover_cell_vod_home);

            Glide.with(holder.getContext())
                    .load(mData.getVod_pic())
                    .into(ivCover);

            tvName.setText(mData.getVod_name());
            tvScore.setText(mData.getVod_score());

            if (mData.getVod_serial().equals("0") || mData.getVod_serial().equals("")) {
                tvRecentWords.setText(mData.getVod_remarks());
            }else {
                tvRecentWords.setText(mData.getVod_serial());
            }

//            if (mData.getVod_play_list().size() > 0 && mData.getVod_play_list().get(0).getUrls().size() > 0) {
//                tvRecentWords.setText(mData.getVod_play_list().get(0).getUrls().get(0).getName());
//            }

            tvSummary.setText(mData.getVod_year());
        }catch (Exception e) {
            Log.d("VodCell",e.getMessage());
        }

        if (mListener != null){
            holder.itemView.setOnClickListener(v -> mListener.onClickItem(mData,position));
        }
    }
}
