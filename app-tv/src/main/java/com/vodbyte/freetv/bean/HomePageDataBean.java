package com.vodbyte.freetv.bean;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class HomePageDataBean {

    private List<ItemBean> item_list;

    public List<ItemBean> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<ItemBean> item_list) {
        this.item_list = item_list;
    }
}
