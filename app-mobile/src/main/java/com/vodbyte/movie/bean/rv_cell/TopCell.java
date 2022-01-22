package com.vodbyte.movie.bean.rv_cell;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.utils.ScreenUtil;

public class TopCell extends BaseRvCell<VodBean> {

    private boolean isGrid = false;

    public TopCell(VodBean vodBean) {
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
        return ItemType.TYPE_TOP_CELL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_top_item,parent,false);

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int width = ScreenUtil.getScreenWidth(parent.getContext()) / 3;
        layoutParams.height = width + (width / 2) - ScreenUtil.dip2px(parent.getContext(),30);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.bottomMargin = ScreenUtil.dip2px(parent.getContext(),2);
        view.setLayoutParams(layoutParams);

        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {

        try {

            ImageView ivCover = holder.getImageView(R.id.iv_cover_cell_vod);

            RequestOptions options = new RequestOptions().error(R.drawable.ic_launcher).bitmapTransform(new RoundedCorners(25));//图片圆角为30

            Glide.with(holder.getContext())
                    .applyDefaultRequestOptions(options)
                    .load(mData.getVod_pic())
                    .into(ivCover);

            TextView tvName = holder.getTextView(R.id.tv_name_cell_vod_home);
            TextView tvName2 = holder.getTextView(R.id.tv_name_cell2);
            TextView tvName3 = holder.getTextView(R.id.tv_name_cell3);
            TextView tvName4 = holder.getTextView(R.id.tv_name_cell4);
            TextView tvScore = holder.getTextView(R.id.tv_score_cell_vod_home);
            TextView tvRecentWords = holder.getTextView(R.id.tv_recentWords_cell_vod_home);

            tvName.setText(mData.getVod_name());
            tvName2.setText(String.format("%s·%s·%s",mData.getVod_year(),mData.getVod_area(),mData.getVod_class()));

            if (mData.getVod_director().isEmpty()) {
                tvName3.setVisibility(View.GONE);
            }else {
                tvName3.setText(String.format("导演:%s",mData.getVod_director()));
            }
            if (mData.getVod_actor().isEmpty()) {
                tvName4.setVisibility(View.GONE);
            }else {
                tvName4.setText(String.format("主演:%s",mData.getVod_actor()));
            }
            tvScore.setText(mData.getVod_score());

            if (mData.getVod_serial().equals("0") || mData.getVod_serial().equals("")) {
                tvRecentWords.setText(mData.getVod_remarks());
            }else {
                tvRecentWords.setText(mData.getVod_serial());
            }

        }catch (Exception e) {
            Log.d("VodCell",e.getMessage());
        }

        if (mListener != null){
            holder.itemView.setOnClickListener(v -> mListener.onClickItem(mData,position));
        }
    }
}
