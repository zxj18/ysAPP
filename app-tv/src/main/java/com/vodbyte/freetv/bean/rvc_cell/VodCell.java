package com.vodbyte.freetv.bean.rvc_cell;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.app.ItemType;
import com.vodbyte.freetv.bean.VodBean;

import static com.vodbyte.freetv.app.Constant.ANIMATION_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_SCALE;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_SCALE;
import com.vodbyte.freetv.R;
import static com.vodbyte.freetv.app.Constant.ANIMATION_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_SCALE;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_SCALE;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_DURATION;

public class VodCell extends BaseRvCell<VodBean> {

    public VodCell(VodBean vodBean) {
        super(vodBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_CELL;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final BaseRvViewHolder viewHolder = new BaseRvViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_vod_item,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BaseRvViewHolder holder, final int position) {
        setVideoCardFocusAnimator(holder.getItemView(),position);

        try {
            TextView tvName = holder.getTextView(R.id.video_item_tv_title);
//            // 评分
            TextView tvScore = holder.getTextView(R.id.tv_score_cell_vod_home);
            TextView tvRecentWords = holder.getTextView(R.id.tv_recentWords_cell_vod_home);
//            TextView tvSummary = holder.getTextView(R.id.tv_summary_cell_vod_home);
            ImageView ivCover = holder.getImageView(R.id.video_item_iv_image);

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

        }catch (Exception e) {
            Log.d("VodCell",e.getMessage());
        }

        if (mListener != null){
            holder.itemView.setOnClickListener(v -> mListener.onClickItem(mData,position));
        }
    }

    private void setVideoCardFocusAnimator(View view,int position) {

        view.setOnFocusChangeListener((v, hasFocus) -> {
            TextView titleView = (TextView) view.findViewById(R.id.video_item_tv_title);

            if (mListener != null) {
                mLoadMoreRvListener.onLoadMore(view,position);
            }

            if (hasFocus) {
                titleView.setTextColor(Color.WHITE);
                titleView.setSelected(true);

                ValueAnimator animatorFirst = ValueAnimator.ofFloat(1.0f, ANIMATION_ZOOM_OUT_SCALE).setDuration(ANIMATION_DURATION);

                animatorFirst.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        view.setScaleX((float) animation.getAnimatedValue());
                        view.setScaleY((float) animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });

                animatorFirst.setInterpolator(new OvershootInterpolator());
                animatorFirst.start();

                //Play icon 动画
                ValueAnimator animatorPlayIcon = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animatorPlayIcon.addUpdateListener(animation -> {
                    view.findViewById(R.id.video_item_iv_icon).setScaleX((float) animation.getAnimatedValue());
                    view.findViewById(R.id.video_item_iv_icon).setScaleY((float) animation.getAnimatedValue());
                });
                animatorPlayIcon.start();
            } else {
                titleView.setTextColor(Color.parseColor("#B2B6BE"));
                titleView.setSelected(false);

                ValueAnimator animator = ValueAnimator.ofFloat(ANIMATION_ZOOM_OUT_SCALE, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animator.addUpdateListener(animation -> {
                    view.setScaleX((float) animation.getAnimatedValue());
                    view.setScaleY((float) animation.getAnimatedValue());
                });
                animator.start();

                //Play icon 动画
                ValueAnimator animatorPlayIcon = ValueAnimator.ofFloat(1.0f, 0.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animatorPlayIcon.addUpdateListener(animation -> {
                    view.findViewById(R.id.video_item_iv_icon).setScaleX((float) animation.getAnimatedValue());
                    view.findViewById(R.id.video_item_iv_icon).setScaleY((float) animation.getAnimatedValue());
                });
                animatorPlayIcon.start();
            }
        });

    }

    protected void setTypeFocusAnimator(View view) {
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                ValueAnimator animatorFirst = ValueAnimator.ofFloat(1.0f, ANIMATION_ZOOM_IN_SCALE).setDuration(ANIMATION_ZOOM_IN_DURATION);
                ValueAnimator animatorSecond = ValueAnimator.ofFloat(ANIMATION_ZOOM_IN_SCALE, ANIMATION_ZOOM_OUT_SCALE).setDuration(ANIMATION_ZOOM_OUT_DURATION);

                animatorFirst.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        view.setScaleX((float) animation.getAnimatedValue());
                        view.setScaleY((float) animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorSecond.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        view.setScaleX((float) animation.getAnimatedValue());
                        view.setScaleY((float) animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorFirst.start();
                animatorSecond.setStartDelay(ANIMATION_ZOOM_IN_DURATION);
                animatorSecond.start();
            } else {
                ValueAnimator animator = ValueAnimator.ofFloat(ANIMATION_ZOOM_OUT_SCALE, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animator.addUpdateListener(animation -> {
                    view.setScaleX((float) animation.getAnimatedValue());
                    view.setScaleY((float) animation.getAnimatedValue());
                });
                animator.start();
            }
        });
    }
}
