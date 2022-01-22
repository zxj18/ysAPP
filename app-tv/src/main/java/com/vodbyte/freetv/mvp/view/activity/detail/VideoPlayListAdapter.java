package com.vodbyte.freetv.mvp.view.activity.detail;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vodbyte.freetv.R;
import com.vodbyte.freetv.bean.VodBean;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_IN_SCALE;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_DURATION;
import static com.vodbyte.freetv.app.Constant.ANIMATION_ZOOM_OUT_SCALE;

public class VideoPlayListAdapter extends RecyclerView.Adapter<VideoPlayListAdapter.ViewHolder> {

    private Activity activity;
    private List<VodBean.VodPlayBean.UrlBean> urlBeanList;
    OnPartClickListener mListener;

    public VideoPlayListAdapter(Activity activity, List<VodBean.VodPlayBean.UrlBean> partList, OnPartClickListener listener) {
        this.activity = activity;
        this.urlBeanList = partList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.video_detail_item_part, parent, false);
        TextView video_item = view.findViewById(R.id.video_item);
        view.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                video_item.setBackground(activity.getResources().getDrawable(R.drawable.video_detail_content_focus));
                video_item.setTextColor(activity.getResources().getColor(R.color.colorTextFocus));

                ValueAnimator animatorFirst = ValueAnimator.ofFloat(1.0f, ANIMATION_ZOOM_IN_SCALE).setDuration(ANIMATION_ZOOM_IN_DURATION);
                ValueAnimator animatorSecond = ValueAnimator.ofFloat(ANIMATION_ZOOM_IN_SCALE, ANIMATION_ZOOM_OUT_SCALE).setDuration(ANIMATION_ZOOM_OUT_DURATION);

                animatorFirst.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        video_item.setScaleX((float)animation.getAnimatedValue());
                        video_item.setScaleY((float)animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorSecond.addUpdateListener(animation -> {
                    if (view.isFocused()) {
                        video_item.setScaleX((float)animation.getAnimatedValue());
                        video_item.setScaleY((float)animation.getAnimatedValue());
                    } else {
                        animatorFirst.cancel();
                    }
                });
                animatorFirst.start();
                animatorSecond.setStartDelay(ANIMATION_ZOOM_IN_DURATION);
                animatorSecond.start();
            } else {
                video_item.setBackground(activity.getResources().getDrawable(R.drawable.video_detail_content_normal));
                video_item.setTextColor(activity.getResources().getColor(R.color.colorTextNormal));

                ValueAnimator animator = ValueAnimator.ofFloat(ANIMATION_ZOOM_OUT_SCALE, 1.0f).setDuration(ANIMATION_ZOOM_IN_DURATION);
                animator.addUpdateListener(animation -> {
                    video_item.setScaleX((float)animation.getAnimatedValue());
                    video_item.setScaleY((float)animation.getAnimatedValue());
                });
                animator.start();
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.video_item.setText(urlBeanList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return urlBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.video_item)
        TextView video_item;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onPartClick(getPosition());
                }
            });
        }
    }

    interface OnPartClickListener {
        void onPartClick(int position);
    }
}
