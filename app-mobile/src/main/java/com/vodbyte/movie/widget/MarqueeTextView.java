package com.vodbyte.movie.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.vodbyte.movie.R;
import com.vodbyte.movie.bean.ConfigBean;
import com.vodbyte.movie.utils.ScreenUtil;

import java.util.List;

public class MarqueeTextView extends LinearLayout {

    private Context mContext;
    private ViewFlipper viewFlipper;
    private View marqueeTextView;
    private List<ConfigBean.Affiche> mAfficheList;

    private MarqueeTextViewClickListener mMarqueeTextViewClickListener;

    public MarqueeTextView(Context context) {
        super(context);
        mContext = context;
        initBasicView();
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBasicView();
    }

    public void setTextArraysAndClickListener(List<ConfigBean.Affiche> afficheList, MarqueeTextViewClickListener marqueeTextViewClickListener) {
        this.mAfficheList = afficheList;
        this.mMarqueeTextViewClickListener = marqueeTextViewClickListener;
        initMarqueeTextView();
    }

    public void initBasicView() {
        marqueeTextView = LayoutInflater.from(mContext).inflate(R.layout.publicity_layout, null);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(marqueeTextView, layoutParams);
        viewFlipper = (ViewFlipper) marqueeTextView.findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_bottom));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_out_top));
        viewFlipper.startFlipping();
    }

    public void initMarqueeTextView() {
        if (mAfficheList.size() == 0) {
            return;
        }

        int i = 0;
        viewFlipper.removeAllViews();
        while (i < mAfficheList.size()) {
            TextView textView = new TextView(mContext);
            textView.setText(mAfficheList.get(i).getTitle());
            textView.setTextSize(ScreenUtil.sp2px(mContext,6));
            if (mMarqueeTextViewClickListener != null) {
                int finalI = i;
                textView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMarqueeTextViewClickListener.onClick(mAfficheList.get(finalI).getTitle(),mAfficheList.get(finalI).getContent());
                    }
                });
            }
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            viewFlipper.addView(textView, lp);
            i++;
        }
    }

    public void releaseResources() {
        if (marqueeTextView != null) {
            if (viewFlipper != null) {
                viewFlipper.stopFlipping();
                viewFlipper.removeAllViews();
                viewFlipper = null;
            }
            marqueeTextView = null;
        }
    }

    public interface MarqueeTextViewClickListener {
        void onClick(String title,String content);
    }

}