package com.vodbyte.freetv.bean.rvc_cell;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vodbyte.easy_rv_adapter.base.BaseRvCell;
import com.vodbyte.easy_rv_adapter.base.BaseRvViewHolder;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.app.ItemType;
import com.vodbyte.freetv.bean.VodBean;

import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_SCALE;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_SCALE;

/**
 * 换源CELL
 */
public class SubSourceCell extends BaseRvCell<VodBean.VodPlayBean> {

    private TextView mTitleView;

    public SubSourceCell(VodBean.VodPlayBean vodPlayBean) {
        super(vodPlayBean);
    }

    @Override
    public void releaseResource() {

    }

    @Override
    public int getItemType() {
        return ItemType.TYPE_VOD_CELL_SUBSET_NUM;
    }

    @Override
    public BaseRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_sub_source_view,parent,false);
        return new BaseRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRvViewHolder holder, final int position) {
        mTitleView = holder.getTextView(R.id.tv_play_source_cell);
        if (!mData.getNote().equals("")) {
            mTitleView.setText(mData.getNote());
        }else {
            mTitleView.setText(mData.getFrom());
        }

        if (mListener != null){
            holder.itemView.setOnClickListener(v -> mListener.onClickItem(mTitleView.getText().toString(),position));
        }

        holder.getItemView().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mTitleView.setBackground(holder.getContext().getResources().getDrawable(R.drawable.video_detail_content_focus));
                mTitleView.setTextColor((holder.getContext().getResources().getColor(R.color.colorTextFocus)));

                ValueAnimator animatorFirst = ValueAnimator.ofFloat(1.0f, ANIMATION_ZOOM_IN_SCALE).setDuration(ANIMATION_ZOOM_IN_DURATION);
                ValueAnimator animatorSecond = ValueAnimator.ofFloat(ANIMATION_ZOOM_IN_SCALE, ANIMATION_ZOOM_OUT_SCALE).setDuration(ANIMATION_ZOOM_OUT_DURATION);

                animatorFirst.addUpdateListener(animation -> {
                    if (holder.getItemView().isFocused()) {
                        mTitleView.setScaleX((float)animation.getAnimatedValue());
                        mTitleView.setScaleY((float)animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorSecond.addUpdateListener(animation -> {
                    if (holder.getItemView().isFocused()) {
                        mTitleView.setScaleX((float)animation.getAnimatedValue());
                        mTitleView.setScaleY((float)animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorFirst.start();
                animatorSecond.setStartDelay(ANIMATION_ZOOM_IN_DURATION);
                animatorSecond.start();
            } else {
                mTitleView.setBackground((holder.getContext().getResources().getDrawable(R.drawable.video_detail_content_normal)));
                mTitleView.setTextColor((holder.getContext().getResources().getColor(R.color.colorTextNormal)));

                ValueAnimator animator = ValueAnimator.ofFloat(ANIMATION_ZOOM_OUT_SCALE, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animator.addUpdateListener(animation -> {
                    mTitleView.setScaleX((float)animation.getAnimatedValue());
                    mTitleView.setScaleY((float)animation.getAnimatedValue());
                });
                animator.start();
            }
        });

    }
}
