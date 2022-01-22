package com.vodbyte.freetv.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.material.tabs.TabLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TvTabLayout extends TabLayout {
    public TvTabLayout(@NonNull Context context) {
        this(context, null);
    }

    public TvTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvTabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //子View优先处理焦点
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
    }

    @NonNull
    @Override
    public Tab newTab() {
        Tab tab = super.newTab();
        tab.view.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                tab.select();
            }
        });
        return tab;
    }
}