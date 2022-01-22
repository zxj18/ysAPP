package com.vodbyte.movie.bean;

import androidx.annotation.Keep;

@Keep
public class HotKeyBean {

    private String key;
    private int colorResId;

    public String getKey() {
        return key == null ? "" : key;
    }

    public void setKey(String key) {
        this.key = key == null ? "" : key;
    }

    public int getColorResId() {
        return colorResId;
    }

    public void setColorResId(int colorResId) {
        this.colorResId = colorResId;
    }
}
