package com.vodbyte.freetv.bean;

import androidx.annotation.Keep;

import org.litepal.crud.LitePalSupport;

@Keep
public class SearchHistoryBean extends LitePalSupport {

    private String key;
    public SearchHistoryBean(String key) {
        this.key = key;
    }
    public String getKey() {
        return key == null ? "" : key;
    }
    public void setKey(String key) {
        this.key = key == null ? "" : key;
    }
}
