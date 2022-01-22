package com.vodbyte.movie.bean;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class HomePageDataBean {

    private List<BannerBean> banner_list;
    private List<ItemBean> item_list;

    public List<BannerBean> getBanner_list() {
        return banner_list;
    }

    public void setBanner_list(List<BannerBean> banner_list) {
        this.banner_list = banner_list;
    }

    public List<ItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemBean> item_list) {
        this.item_list = item_list;
    }
}
