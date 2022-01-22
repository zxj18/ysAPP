package com.vodbyte.movie.mvp.model.dto;

import androidx.annotation.Keep;

import com.vodbyte.movie.bean.BannerBean;
import com.vodbyte.movie.bean.HomePageDataBean;
import com.vodbyte.movie.bean.ItemBean;
import com.vodbyte.movie.bean.VodBean;

import java.util.List;

@Keep
public class HomeVodBeanListDataDTO {

    private Integer code;
    private String msg;
    private HomeVodBeanListData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HomeVodBeanListData getData() {
        return data;
    }

    public void setData(HomeVodBeanListData data) {
        this.data = data;
    }



    @Keep
    public static class HomeVodBeanListData {

        private List<BannerBean> banner_list;
        private List<VodBean> item_list;

        public List<BannerBean> getBanner_list() {
            return banner_list;
        }

        public void setBanner_list(List<BannerBean> banner_list) {
            this.banner_list = banner_list;
        }

        public List<VodBean> getItem_list() {
            return item_list;
        }

        public void setItem_list(List<VodBean> item_list) {
            this.item_list = item_list;
        }
    }
}
