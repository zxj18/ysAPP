package com.vodbyte.freetv.bean.rvc_cell;

import android.animation.ValueAnimator;
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
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.app.ItemType;
import com.vodbyte.freetv.bean.ClassifyTitleBean;

import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_SCALE;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_SCALE;

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
        return createSimpleHolder(parent, R.layout.cell_cid_label);
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        TextView tvCidLabel = holder.getTextView(R.id.tv_cid_label);
        tvCidLabel.setText(mData);

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvCidLabel.setBackground(holder.getContext().getResources().getDrawable(R.drawable.video_detail_content_focus));
                    tvCidLabel.setTextColor((holder.getContext().getResources().getColor(R.color.colorText)));

                    ValueAnimator animatorFirst = ValueAnimator.ofFloat(1.0f, ANIMATION_ZOOM_IN_SCALE).setDuration(ANIMATION_ZOOM_IN_DURATION);
                    ValueAnimator animatorSecond = ValueAnimator.ofFloat(ANIMATION_ZOOM_IN_SCALE, ANIMATION_ZOOM_OUT_SCALE).setDuration(ANIMATION_ZOOM_OUT_DURATION);

                    animatorFirst.addUpdateListener(animation -> {
                        if (holder.getItemView().isFocused()) {
                            tvCidLabel.setScaleX((float)animation.getAnimatedValue());
                            tvCidLabel.setScaleY((float)animation.getAnimatedValue());
                        } else {
                            animatorFirst.cancel();
                        }
                    });
                    animatorSecond.addUpdateListener(animation -> {
                        if (holder.getItemView().isFocused()) {
                            tvCidLabel.setScaleX((float)animation.getAnimatedValue());
                            tvCidLabel.setScaleY((float)animation.getAnimatedValue());
                        } else {
                            animatorFirst.cancel();
                        }
                    });
                    animatorFirst.start();
                    animatorSecond.setStartDelay(ANIMATION_ZOOM_IN_DURATION);
                    animatorSecond.start();
                } else {
                    tvCidLabel.setBackground((holder.getContext().getResources().getDrawable(R.drawable.video_detail_content_normal)));
                    tvCidLabel.setTextColor((holder.getContext().getResources().getColor(R.color.colorTextNormal)));

                    ValueAnimator animator = ValueAnimator.ofFloat(ANIMATION_ZOOM_OUT_SCALE, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                    animator.addUpdateListener(animation -> {
                        tvCidLabel.setScaleX((float)animation.getAnimatedValue());
                        tvCidLabel.setScaleY((float)animation.getAnimatedValue());
                    });
                    animator.start();
                }
            }
        });

        if (position == 0) {
            holder.itemView.setBackground(holder.getContext().getResources().getDrawable(R.drawable.video_detail_content_focus));
            tvCidLabel.setTextColor(Color.parseColor("#000000"));
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
