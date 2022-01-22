package com.vodbyte.movie.utils;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vodbyte.movie.R;

public class AnimateUtil {
    /**
     * 点击喜欢时的动画
     * @param view
     * @param isLike 目前的状态（没点击前）
     */
    public static void likeAnimate(final View view, boolean isLike) {
//        if (isLike){
//            //取消喜欢
//            view.setImageResource(R.drawable.svg_red_like_normal);
//        }else {
//            //喜欢
//            view.setImageResource(R.drawable.svg_red_like_pressed);
//        }
        view.animate().scaleX(1.2f).scaleY(1.2f).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.animate().scaleX(1f).scaleY(1f);
            }
        });
    }
}
