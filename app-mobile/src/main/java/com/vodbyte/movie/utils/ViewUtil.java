package com.vodbyte.movie.utils;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtil {
    /**
     * 从父 view 中移除自己
     * @param child
     */
    public static void removeSelfFromParent(View child){
        if (child != null){
            ViewGroup parent = (ViewGroup)child.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                parent.removeView(child);
            }
        }
    }
}
