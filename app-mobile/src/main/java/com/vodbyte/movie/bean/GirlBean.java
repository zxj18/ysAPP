package com.vodbyte.movie.bean;

import androidx.annotation.Keep;

import com.vodbyte.movie.utils.GsonUtil;

import java.io.Serializable;
import java.util.List;

@Keep
public  class GirlBean implements Serializable {
    private  Integer id;
    private  String title;
    private  String url;
    private  List<String> urls;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}