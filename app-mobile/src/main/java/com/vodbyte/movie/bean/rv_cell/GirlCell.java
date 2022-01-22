package com.vodbyte.movie.bean.rv_cell;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.GirlBean;
import com.vodbyte.movie.bean.VodBean;
import com.vodbyte.movie.config.ItemType;
import com.vodbyte.movie.utils.ScreenUtil;

public class GirlCell extends BaseRvCell<GirlBean> {

    private boolean isGrid = false;

    public GirlCell(GirlBean girlBean) {
        super(girlBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_GIRL_CELL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_girl_item,parent,false);

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int width = ScreenUtil.getScreenWidth(parent.getContext()) / 2;
        layoutParams.height = width + (width / 2);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.bottomMargin = ScreenUtil.dip2px(parent.getContext(),2);
        view.setLayoutParams(layoutParams);

        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {

        try {

            TextView tvName = holder.getTextView(R.id.tv_girl_title);
            TextView tvNum = holder.getTextView(R.id.tv_girl_num);
            ImageView ivCover = holder.getImageView(R.id.iv_cover_girl);

            Glide.with(holder.getContext())
                    .load(mData.getUrl())
                    .into(ivCover);

            tvName.setText(mData.getTitle());
            tvNum.setText(mData.getUrls().size() + "P");

        }catch (Exception e) {
            Log.d("VodCell",e.getMessage());
        }

        if (mListener != null){
            holder.itemView.setOnClickListener(v -> mListener.onClickItem(mData,position));
        }
    }
}
